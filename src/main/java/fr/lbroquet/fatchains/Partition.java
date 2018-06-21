package fr.lbroquet.fatchains;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

public class Partition implements Closeable {

    private static final System.Logger LOG = System.getLogger(Partition.class.getName());
    private static final int BYTES_PER_SECTOR = 512;

    private final Path path;
    private final FileChannel channel;
    private BootSector bootSector;
    private FatEntries fat;

    public Partition(Path path) throws IOException {
        this.path = path;
        this.channel = FileChannel.open(path, StandardOpenOption.READ);
    }

    public String getFileName() {
        return path.getFileName().toString();
    }

    public BootSector getBootSector() throws IOException {
        return Optional.ofNullable(bootSector).orElse(readAndCacheBootSector());
    }

    private BootSector readAndCacheBootSector() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BYTES_PER_SECTOR);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        readChannel(0, buffer);
        bootSector = new BootSector(buffer.rewind());
        return bootSector;
    }

    public FatEntries getFat() throws IOException {
        return Optional.ofNullable(fat).orElse(readAndCacheFat());
    }

    private FatEntries readAndCacheFat() throws IOException {
        BootSector bootSector = getBootSector();
        ByteBuffer buffer = ByteBuffer.allocate((int) bootSector.getFatLengthInBytes());
        readChannel(bootSector.getFatOffsetInBytes(), buffer);
        fat = new FatEntries(buffer);
        return fat;
    }

    public String guessEntryType(int entryIndex) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BYTES_PER_SECTOR);
        readChannel(getHeadClusterPosition(entryIndex), buffer);
        return EntryType.searchSignature(buffer.array());
    }

    private long getHeadClusterPosition(int entryIndex) throws IOException {
        BootSector bootSector = getBootSector();
        return bootSector.getClusterOffset() + (entryIndex - 2) * bootSector.getBytesPerCluster();
    }

    private void readChannel(long position, ByteBuffer buffer) throws IOException {
        LOG.log(System.Logger.Level.INFO, "Reading {0} bytes from {1} @{2}", buffer.remaining(), path, position);
        channel.position(position);
        channel.read(buffer);
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }
}
