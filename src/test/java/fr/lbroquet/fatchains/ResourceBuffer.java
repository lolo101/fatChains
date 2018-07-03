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

public class ResourceBuffer {

    private final ByteBuffer buffer;

    public ResourceBuffer(String name) throws URISyntaxException, IOException {
        URL input = ResourceBuffer.class.getResource(name);
        Path path = Paths.get(input.toURI());
        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            int fileSize = (int) Files.size(path);
            ByteBuffer fileBuffer = ByteBuffer.allocate(fileSize);
            channel.read(fileBuffer);
            this.buffer = fileBuffer;
        }
    }

    public ByteBuffer getBuffer() {
        return buffer.duplicate().rewind();
    }
}
