package fr.lbroquet.fatchains;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FatEntries implements Iterable<FatEntry> {

    private final ByteBuffer buffer;

    FatEntries(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public Iterator<FatEntry> iterator() {
        return new FatEntryIterator(buffer.duplicate().order(ByteOrder.LITTLE_ENDIAN));
    }

    public Stream<FatEntry> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public EntryHeads getHeads() {
        return stream().skip(2).reduce(new EntryHeads(), EntryHeads::consider, EntryHeads::merge);
    }
}

class FatEntryIterator implements Iterator<FatEntry> {

    private final ByteBuffer buffer;
    private int index;
    private int nextEntryIndex;

    FatEntryIterator(ByteBuffer buffer) {
        this.buffer = buffer;
        skipUnallocateds();
    }

    @Override
    public boolean hasNext() {
        return buffer.hasRemaining();
    }

    @Override
    public FatEntry next() {
        FatEntry fatEntry = new FatEntry(index++, nextEntryIndex);
        skipUnallocateds();
        return fatEntry;
    }

    @SuppressWarnings("empty-statement")
    private void skipUnallocateds() {
        for (;buffer.hasRemaining() && (nextEntryIndex = buffer.getInt()) == 0; ++index);
    }
}
