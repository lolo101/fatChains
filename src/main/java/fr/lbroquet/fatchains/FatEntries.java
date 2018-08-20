package fr.lbroquet.fatchains;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class FatEntries {

    private final SortedMap<Integer, FatEntry> allocateds = new TreeMap<>();
    private final Collection<Integer> pointedAt = new TreeSet<>();

    FatEntries(ByteBuffer buffer) {
        for (FatEntryIterator fatEntryIterator = new FatEntryIterator(buffer.order(ByteOrder.LITTLE_ENDIAN).rewind()); fatEntryIterator.hasNext();) {
            FatEntry entry = fatEntryIterator.next();
            allocateds.put(entry.getIndex(), entry);
            pointedAt.add(entry.getNextEntryIndex());
        }
    }

    public List<EntryChain> getChains() {
        return allocateds.values().stream()
                .skip(2)
                .filter(this::notPointedAt)
                .map(this::toEntryChain)
                .collect(Collectors.toList());
    }

    private boolean notPointedAt(FatEntry e) {
        return !pointedAt.contains(e.getIndex());
    }

    private EntryChain toEntryChain(FatEntry e) {
        return new EntryChain(e.getIndex(), allocateds);
    }
}
