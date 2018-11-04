package fr.lbroquet.fatchains;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;

/**
 * This class represents an exFAT partition.
 * The partition holds a set of objects :
 * <ul>
 * <li>A Boot sector, holding information about the structure of the partition</li>
 * <li>A File Allocation Table (FAT) where the chains of clusters holding the files contents are referenced.</li>
 * <li>A Cluster heap holding the actual content of files.</li>
 * </ul>
 */
public class Partition implements Closeable {

    private static final System.Logger LOG = System.getLogger(Partition.class.getName());
    private static final int BYTES_PER_SECTOR = 512;

    private final FileChannel channel;
    private BootSector bootSector;
    private List<EntryChain> entryChains;

    public Partition(Path path) throws IOException {
        this.channel = FileChannel.open(path, StandardOpenOption.READ);
    }

    public BootSector getBootSector() {
        return Optional.ofNullable(bootSector).orElseGet(() -> readAndCacheBootSector());
    }

    public List<EntryChain> getEntryChains() {
        return Optional.ofNullable(entryChains).orElseGet(() -> readAndCacheEntryChains());
    }

    public ByteBuffer readCluster(FatEntry entry) {
        BootSector bootSector = getBootSector();
        long position = bootSector.getClusterPosition(entry);
        ByteBuffer buffer = ByteBuffer.allocate((int) bootSector.getBytesPerCluster());
        readChannel(position, buffer);
        return buffer.rewind();
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }

    private BootSector readAndCacheBootSector() {
        ByteBuffer buffer = ByteBuffer.allocate(BYTES_PER_SECTOR);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        readChannel(0, buffer);
        bootSector = new BootSector(buffer);
        return bootSector;
    }

    private List<EntryChain> readAndCacheEntryChains() {
        BootSector bootSector = getBootSector();
        ByteBuffer buffer = ByteBuffer.allocate((int) bootSector.getFatLengthInBytes());
        readChannel(bootSector.getFatOffsetInBytes(), buffer);
        entryChains = new FatEntries(this, buffer).getChains();
        return entryChains;
    }

    private void readChannel(long position, ByteBuffer buffer) {
        try {
            LOG.log(System.Logger.Level.INFO, "Reading {0} bytes @{1}", buffer.remaining(), position);
            channel.position(position);
            channel.read(buffer);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
