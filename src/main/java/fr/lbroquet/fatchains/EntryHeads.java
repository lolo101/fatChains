package fr.lbroquet.fatchains;

import java.util.Collection;
import java.util.TreeSet;
import java.util.stream.Stream;

public class EntryHeads {

    private final Collection<Integer> pointedAt = new TreeSet<>();
    private final Collection<FatEntry> allocateds = new TreeSet<>();

    public EntryHeads consider(FatEntry entry) {
        allocateds.add(entry);
        pointedAt.add(entry.getNextEntryIndex());
        return this;
    }

    public EntryHeads merge(EntryHeads otherHeads) {
        allocateds.addAll(otherHeads.allocateds);
        pointedAt.addAll(otherHeads.pointedAt);
        return this;
    }

    public Stream<EntryChain> chains() {
        return allocateds.stream()
                .filter(this::notPointedAt)
                .map(this::toEntryChain);
    }

    private boolean notPointedAt(FatEntry e) {
        return !pointedAt.contains(e.getIndex());
    }

    private EntryChain toEntryChain(FatEntry e) {
        return new EntryChain(e.getIndex(), allocateds);
    }
}
