package fr.lbroquet.fatchains;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Stream;

public class EntryChain {

    private final List<FatEntry> chain = new ArrayList<>();

    EntryChain(int head, SortedMap<Integer, FatEntry> entries) {
        for (int index = head; entries.containsKey(index) ; index = entries.get(index).getNextEntryIndex()) {
            chain.add(entries.get(index));
        }
    }

    public int getHead() {
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
}
