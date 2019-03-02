package fr.lbroquet.fatchains;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

public class FatFile {

    private static final URL BOOT = FatFile.class.getClassLoader().getResource("boot.bin");
    private final ResourceBuffer buffer;
    private final Partition partition;

    public FatFile(String name) throws URISyntaxException, IOException {
        this.buffer = new ResourceBuffer(name);
        this.partition = new Partition(Paths.get(BOOT.toURI()));
    }

    public List<EntryChain> readChains() {
        return new FatEntries(partition, buffer.getBuffer()).getChains();
    }
}
