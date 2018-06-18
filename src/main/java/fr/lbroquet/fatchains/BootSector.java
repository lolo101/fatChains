package fr.lbroquet.fatchains;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import lombok.Value;

@Value
public class BootSector {

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

    BootSector(ByteBuffer buffer) {
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

    public long getFatOffsetInBytes() {
        return fatOffset << bytesPerSectorExposant;
    }

    public long getFatLengthInBytes() {
        return fatLength << bytesPerSectorExposant;
    }
}
