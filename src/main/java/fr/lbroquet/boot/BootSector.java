package fr.lbroquet.boot;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import lombok.Value;

@Value
public class BootSector {

    private static final int BYTES_PER_SECTOR = 512;

    final byte[] jmp = new byte[3];
    final byte[] name = new byte[8];
    final long partitionOffset;
    final long volumeLength;
    final int fatOffset;
    final int fatLength;
    final int clusterOffset;
    final int clusterCount;
    final int rootCluster;
    final int volumeSN;
    final byte[] fsRev = new byte[2];
    final short flags;
    final byte bytesPerSectorExposant;
    final byte sectorPerClusterExposant;
    final byte nbFats;
    final byte driveSelect;
    final byte pctUse;

    public static BootSector from(Path path) throws IOException {
        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(BYTES_PER_SECTOR);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            channel.read(buffer);
            return new BootSector(buffer.rewind());
        }
    }

    private BootSector(ByteBuffer buffer) {
        buffer.get(jmp);
        buffer.get(name);
        buffer.position(64);
        partitionOffset = buffer.getLong();
        volumeLength = buffer.getLong();
        fatOffset = buffer.getInt();
        fatLength = buffer.getInt();
        clusterOffset = buffer.getInt();
        clusterCount = buffer.getInt();
        rootCluster = buffer.getInt();
        volumeSN = buffer.getInt();
        buffer.get(fsRev);
        flags = buffer.getShort();
        bytesPerSectorExposant = buffer.get();
        sectorPerClusterExposant = buffer.get();
        nbFats = buffer.get();
        driveSelect = buffer.get();
        pctUse = buffer.get();
    }

    public String getName() {
        return new String(name, Charset.forName("ASCII"));
    }

    public int getBytesPerSector() {
        return 1 << bytesPerSectorExposant;
    }

    public int getSectorPerCluster() {
        return 1 << sectorPerClusterExposant;
    }

    public int getBytesPerCluster() {
        return 1 << (bytesPerSectorExposant + sectorPerClusterExposant);
    }

    public long getVolumeLengthInBytes() {
        return volumeLength << bytesPerSectorExposant;
    }
}
