package fr.lbroquet.fatchains;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Stream;

public class EntryChain {

    private final int head;
    private final List<FatEntry> chain = new ArrayList<>();

    EntryChain(int head, SortedMap<Integer, FatEntry> entries) {
        this.head = head;
        buildEntryList(entries);
    }

    private void buildEntryList(SortedMap<Integer, FatEntry> entries) {
        for (int next = head; entries.containsKey(next) ; next = entries.get(next).getNextEntryIndex()) {
            chain.add(entries.get(next));
        }
    }

    public int getHead() {
        return head;
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
