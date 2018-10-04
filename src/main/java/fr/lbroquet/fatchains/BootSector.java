package fr.lbroquet.fatchains;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import lombok.Value;

@Value
public class BootSector {

    private static final byte[] VALID_SIGNATURE = {0x55, -0x56};

    final byte[] jmp = new byte[3];
    final byte[] name = new byte[8];
    final long partitionOffset;
    final long volumeLength;
    final int fatOffset;
    final int fatLength;
    final long clusterOffset;
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

    BootSector(ByteBuffer buffer) throws IOException {
        validityCheck(buffer);
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

    private void validityCheck(ByteBuffer buffer) throws IOException {
        byte[] signature = new byte[2];
        buffer.position(510);
        buffer.get(signature);
        buffer.position(0);
        if (!Arrays.equals(signature, VALID_SIGNATURE)) {
            throw new IOException(String.format("Invalid Signature %02x %02x", signature[0], signature[1]));
        }
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

    public long getBytesPerCluster() {
        return 1 << (bytesPerSectorExposant + sectorPerClusterExposant);
    }

    public long getVolumeLengthInBytes() {
        return volumeLength << bytesPerSectorExposant;
    }

    public long getFatOffsetInBytes() {
        return fatOffset << bytesPerSectorExposant;
    }

    public long getFatLengthInBytes() {
        return fatLength << bytesPerSectorExposant;
    }

    public long getClusterOffsetInBytes() {
        return clusterOffset << bytesPerSectorExposant;
    }
}
