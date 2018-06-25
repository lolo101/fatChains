package fr.lbroquet.fatchains;

import java.util.Iterator;
import java.util.SortedMap;

class EntryChainIterator implements Iterator<FatEntry> {

    private final SortedMap<Integer, FatEntry> entries;
    private int next;

    EntryChainIterator(int head, SortedMap<Integer, FatEntry> entries) {
        this.entries = entries;
        this.next = head;
    }

    @Override
    public boolean hasNext() {
        return entries.containsKey(next);
    }

    @Override
    public FatEntry next() {
        FatEntry current = entries.get(next);
        next = current.getNextEntryIndex();
        return current;
    }
}
