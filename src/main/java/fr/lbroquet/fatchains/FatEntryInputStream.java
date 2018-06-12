package fr.lbroquet.fatchains;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

class FatEntryInputStream implements Closeable, Iterable<FatEntry> {

    private final InputStream input;
    private int entryIndex = 0;

    FatEntryInputStream(InputStream input) {
        this.input = input;
    }

    @Override
    public void close() throws IOException {
        input.close();
    }

    @Override
    public Iterator<FatEntry> iterator() {
        return new Iterator<FatEntry>() {
            private FatEntry next = tryNextEntry();
            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public FatEntry next() {
                FatEntry current = next;
                next = tryNextEntry();
                return current;
            }
        };
    }

    private FatEntry tryNextEntry() {
        try {
            return nextEntry();
        } catch(IOException ex) {
            System.err.println(ex);
            return null;
        }
    }

    FatEntry nextEntry() throws IOException {
        byte[] b = new byte[4];
        int read = input.read(b);
        if (read == -1) {
            return null;
        }
        if (read < b.length) {
            throw new IOException("Not enough bytes");
        }
        return new FatEntry(entryIndex++, b);
    }

}
