package fr.lbroquet.fatchains;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

class FatEntryInputStream implements Closeable {

    private final InputStream input;
    private int entryIndex = 0;

    FatEntryInputStream(InputStream input) {
        this.input = input;
    }

    @Override
    public void close() throws IOException {
        input.close();
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
