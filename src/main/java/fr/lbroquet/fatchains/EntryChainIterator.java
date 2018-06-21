package fr.lbroquet.fatchains;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

class EntryChainIterator implements Iterator<FatEntry> {

    private final SortedMap<Integer, FatEntry> entries;
    private int next;

    EntryChainIterator(int head, Collection<FatEntry> entries) {
        this.entries = entries.stream().collect(toMap(FatEntry::getIndex, identity(), (v1, v2) -> v1, TreeMap::new));
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
