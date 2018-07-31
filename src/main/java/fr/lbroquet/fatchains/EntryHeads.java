package fr.lbroquet.fatchains;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class EntryHeads {

    private final SortedMap<Integer, FatEntry> allocateds = new TreeMap<>();
    private final Collection<Integer> pointedAt = new TreeSet<>();

    public EntryHeads consider(FatEntry entry) {
        allocateds.put(entry.getIndex(), entry);
        pointedAt.add(entry.getNextEntryIndex());
        return this;
    }

    public EntryHeads merge(EntryHeads otherHeads) {
        allocateds.putAll(otherHeads.allocateds);
        pointedAt.addAll(otherHeads.pointedAt);
        return this;
    }

    public List<EntryChain> chains() {
        return allocateds.values().stream()
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
