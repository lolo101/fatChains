package fr.lbroquet.fatchains;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

class EntryHeads {

    private final SortedMap<Integer, FatEntry> pointedAt = new TreeMap<>();
    private final SortedMap<Integer, FatEntry> allocateds = new TreeMap<>();

    public EntryHeads consider(FatEntry entry) {
        allocateds.put(entry.getIndex(), entry);
        pointedAt.put(entry.getNextEntryIndex(), entry);
        return this;
    }

    public EntryHeads merge(EntryHeads otherHeads) {
        allocateds.putAll(otherHeads.allocateds);
        pointedAt.putAll(otherHeads.pointedAt);
        return this;
    }

    public Collection<EntryChain> chains() {
        Collection<Integer> heads = new ArrayList<>(allocateds.keySet());
        heads.removeAll(pointedAt.keySet());

        return heads.stream().map(h -> new EntryChain(h, allocateds)).collect(Collectors.toList());
    }
}
