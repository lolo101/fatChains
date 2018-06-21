package fr.lbroquet.fatchains;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EntryChain implements Iterable<FatEntry> {

    private final int head;
    private final Collection<FatEntry> entries;

    EntryChain(int head, Collection<FatEntry> entries) {
        this.head = head;
        this.entries = entries;
    }

    public int getHead() {
        return head;
    }

    @Override
    public Iterator<FatEntry> iterator() {
        return new EntryChainIterator(head, entries);
    }

    public Stream<FatEntry> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public boolean isFinished() {
        return stream().anyMatch(FatEntry::isLastOfChain);
    }

    public long length() {
        return stream().count();
    }
}
