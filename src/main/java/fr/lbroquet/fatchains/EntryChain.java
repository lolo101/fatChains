package fr.lbroquet.fatchains;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.stream.Stream;

public class EntryChain {

    private final Partition partition;
    private final List<FatEntry> chain = new ArrayList<>();
    private String type = null;

    EntryChain(Partition partition, int head, SortedMap<Integer, FatEntry> entries) {
        this.partition = partition;
        for (int index = head; entries.containsKey(index) ; index = entries.get(index).getNextEntryIndex()) {
            chain.add(entries.get(index));
        }
    }

    public int getClusterIndex() {
        return chain.get(0).getIndex() - 2;
    }

    public Stream<FatEntry> stream() {
        return chain.stream();
    }

    public boolean isFinished() {
        return chain.get(chain.size() - 1).isLastOfChain();
    }

    public long length() {
        return chain.size();
    }

    public String getType() {
        return Optional.ofNullable(type).orElseGet(() -> getAndCacheType());
    }

    private String getAndCacheType() {
        try {
            type = partition.guessEntryType(this);
            return type;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
