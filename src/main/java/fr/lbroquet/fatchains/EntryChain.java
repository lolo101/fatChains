package fr.lbroquet.fatchains;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EntryChain implements Iterable<FatEntry> {

    private int next;
    private final SortedMap<Integer, FatEntry> entries;

    EntryChain(int head, SortedMap<Integer, FatEntry> entries) {
        this.next = head;
        this.entries = entries;
    }

    public Stream<FatEntry> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public Iterator<FatEntry> iterator() {
        return new Iterator<FatEntry>() {
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
        };
    }
}
