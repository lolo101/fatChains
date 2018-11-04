package fr.lbroquet.fatchains;

import java.nio.ByteBuffer;
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

    public int getHeadEntryIndex() {
        return chain.get(0).getIndex();
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
        ByteBuffer cluster = partition.readCluster(chain.get(0));
        type = EntryType.searchSignature(cluster.array());
        return type;
    }
}
