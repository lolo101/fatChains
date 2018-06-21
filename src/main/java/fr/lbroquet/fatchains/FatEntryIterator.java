package fr.lbroquet.fatchains;

import java.nio.ByteBuffer;
import java.util.Iterator;

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
