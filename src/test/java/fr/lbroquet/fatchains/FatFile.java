package fr.lbroquet.fatchains;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;

public class FatFile {

    private final URL input;

    public FatFile(URL input) {
        this.input = input;
    }

    public Collection<EntryChain> findChains() throws URISyntaxException, IOException {
        Path path = Paths.get(input.toURI());
        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            int fileSize = (int) Files.size(path);
            ByteBuffer fileBuffer = ByteBuffer.allocate(fileSize);
            channel.read(fileBuffer);
            FatEntries entries = new FatEntries(fileBuffer.rewind());
            return entries.getHeads().chains();
        }
    }
}
