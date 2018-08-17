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

    private final Path path;
    private final FileChannel channel;
    private BootSector bootSector;
    private List<EntryChain> entryChains;

    public Partition(Path path) throws IOException {
        this.path = path;
        this.channel = FileChannel.open(path, StandardOpenOption.READ);
    }

    public String getFileName() {
        return path.getFileName().toString();
    }

    public BootSector getBootSector() throws IOException {
        return Optional.ofNullable(bootSector).orElseGet(() -> readAndCacheBootSector());
    }

    private BootSector readAndCacheBootSector() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(BYTES_PER_SECTOR);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            readChannel(0, buffer);
            bootSector = new BootSector(buffer.rewind());
            return bootSector;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public List<EntryChain> getEntryChains() throws IOException {
        return Optional.ofNullable(entryChains).orElseGet(() -> readAndCacheEntryChains());
    }

    private List<EntryChain> readAndCacheEntryChains() {
        try {
            BootSector bootSector = getBootSector();
            ByteBuffer buffer = ByteBuffer.allocate((int) bootSector.getFatLengthInBytes());
            readChannel(bootSector.getFatOffsetInBytes(), buffer);
            entryChains = new FatEntries(buffer).getChains();
            return entryChains;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public String guessEntryType(EntryChain chain) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BYTES_PER_SECTOR);
        readChannel(getHeadClusterPosition(chain), buffer);
        return EntryType.searchSignature(buffer.array());
    }

    private long getHeadClusterPosition(EntryChain chain) throws IOException {
        BootSector bootSector = getBootSector();
        final int headOffset = chain.getHead() - 2;
        return bootSector.getClusterOffsetInBytes() + headOffset * bootSector.getBytesPerCluster();
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
