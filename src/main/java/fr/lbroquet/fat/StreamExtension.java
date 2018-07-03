package fr.lbroquet.fat;

import java.nio.ByteBuffer;
import lombok.Value;

@Value
public class StreamExtension {

    private byte nameLength;
    private long validDataLength;
    private int firstCluster;

    public StreamExtension(ByteBuffer buffer) {
        byte tag = buffer.get();
        if (tag != 0xffffffc0) {
            throw new IllegalArgumentException(String.format("Invalid tag: %02x", tag));
        }

        short flags = buffer.getShort();
        boolean allocatable = (flags & 0x0001) != 0;
        boolean contiguousClusters = (flags & 0x0002) != 0;

        nameLength = buffer.get();
        short checksum = buffer.getShort();
        buffer.getShort();
        validDataLength = buffer.getLong();
        buffer.getInt();
        firstCluster = buffer.getInt();
        long dataLength = buffer.getLong();
    }

}
