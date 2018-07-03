package fr.lbroquet.fatchains;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

public class FatFile {

    private final ResourceBuffer buffer;

    public FatFile(String name) throws URISyntaxException, IOException {
        this.buffer = new ResourceBuffer(name);
    }

    public Stream<EntryChain> readChains() {
        return new FatEntries(buffer.getBuffer()).getHeads().chains();
    }
}
