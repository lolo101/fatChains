package fr.lbroquet.fatchains;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

public class Partition {

    private static final int BYTES_PER_SECTOR = 512;

    private final Path path;
    private BootSector bootSector;
    private FatEntries fat;

    public Partition(Path path) {
        this.path = path;
    }

    public String getFileName() {
        return path.getFileName().toString();
    }

    public BootSector getBootSector() throws IOException {
        return Optional.ofNullable(bootSector).orElse(readAndCacheBootSector());
    }

    private BootSector readAndCacheBootSector() throws IOException {
        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(BYTES_PER_SECTOR);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            channel.read(buffer);
            bootSector = new BootSector(buffer.rewind());
            return bootSector;
        }
    }

    public FatEntries getFat() throws IOException {
        return Optional.ofNullable(fat).orElse(readAndCacheFat());
    }

    private FatEntries readAndCacheFat() throws IOException {
        BootSector bootSector = getBootSector();
        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate((int) bootSector.getFatLengthInBytes());
            channel.position(bootSector.getFatOffsetInBytes());
            channel.read(buffer);
            fat = new FatEntries(buffer);
            return fat;
        }
    }
}
