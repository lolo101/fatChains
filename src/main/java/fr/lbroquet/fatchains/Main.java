package fr.lbroquet.fatchains;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public class Main {
    public static void main(String... args) throws IOException {
        try (FileInputStream fis = new FileInputStream(args[0])) {
            Collection<EntryChain> chains = buildChains(fis);
            System.out.printf("%d complete chains:%n", chains.size());
            chains.stream()
                    //.sorted(Comparator.comparingInt(EntryChain::size).reversed())
                    .forEach(System.out::println);
        }
    }

    static Collection<EntryChain> buildChains(InputStream input) throws IOException {
        try(FatEntryInputStream channel = new FatEntryInputStream(input)) {
            channel.nextEntry();
            channel.nextEntry();
            return Main.buildChains(channel);
        }
    }

    private static Collection<EntryChain> buildChains(FatEntryInputStream channel) throws IOException {
        //SortedMap<Integer, EntryChain> allocateds = new TreeMap<>();
        SortedMap<Integer, EntryChain> heads = new TreeMap<>();
        SortedMap<Integer, EntryChain> tails = new TreeMap<>();
        Collection<EntryChain> interrupteds = new ArrayList<>();
        for (FatEntry entry; (entry = channel.nextEntry()) != null ;) {
            if (entry.isAllocated()) {
                int entryIndex = entry.getIndex();

                EntryChain chainTail = tails.remove(entryIndex);

                if (chainTail == null) {
                    chainTail = new EntryChain();
                    heads.put(entryIndex, chainTail);
                }

                //allocateds.put(entryIndex, chainTail);
                chainTail.push(entry);

                if (!entry.isLastOfChain()) {
                    int nextEntryIndex = entry.getNextEntryIndex();
                    EntryChain chainHead = heads.remove(nextEntryIndex);
                    if (chainHead == null) {
                        EntryChain interrupted = tails.put(nextEntryIndex, chainTail);
                        if (interrupted != null) {
                            interrupteds.add(interrupted);
                        }
                    } else {
                        if (chainTail == chainHead) {
                            throw new IOException("Loop detected");
                        }
                        chainTail.push(chainHead);
                    }
                }
            }
        }
        if (!tails.isEmpty()) {
//            throw new IOException("Unfinished chains remaining");
            System.out.printf("WARN: %d unfinished chains:%n", tails.size());
            tails.values().stream()
                    .forEach(System.out::println);
            tails.values().stream()
                    .map(EntryChain::firstIndex)
                    .forEach(index -> heads.remove(index));
        }
        if (!interrupteds.isEmpty()) {
            System.out.printf("WARN: %d interrupted chains:%n", interrupteds.size());
            interrupteds.stream()
                    .forEach(System.out::println);
            interrupteds.stream()
                    .map(EntryChain::firstIndex)
                    .forEach(index -> heads.remove(index));
        }
        return heads.values();
    }
}
