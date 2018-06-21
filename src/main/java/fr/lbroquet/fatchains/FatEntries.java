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
