package fr.lbroquet.fatchains;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class FatFile {

    private final ResourceBuffer buffer;

    public FatFile(String name) throws URISyntaxException, IOException {
        this.buffer = new ResourceBuffer(name);
    }

    public List<EntryChain> readChains() {
        return new FatEntries(null, buffer.getBuffer()).getChains();
    }
}
