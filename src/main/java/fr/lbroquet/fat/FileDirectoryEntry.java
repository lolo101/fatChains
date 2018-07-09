package fr.lbroquet.fat;

import java.nio.ByteBuffer;
import lombok.Value;

@Value
public class FileDirectoryEntry {

    private final byte secondaryCount;

    public FileDirectoryEntry(ByteBuffer buffer) {
        byte tag = buffer.get();
        if (tag != 0xffffff85) {
            throw new IllegalArgumentException(String.format("Invalid tag: %02x", tag));
        }

        secondaryCount = buffer.get();
        if (secondaryCount < 2 || secondaryCount > 18) {
            throw new IllegalArgumentException(String.format("Secondary count out of range: %d", secondaryCount));
        }

        short checksum = buffer.getShort();

        short fileAttributes = buffer.getShort();
        boolean readOnly = (fileAttributes & 0x0001) != 0;
        boolean hidden = (fileAttributes & 0x0002) != 0;
        boolean system = (fileAttributes & 0x0004) != 0;
        boolean directory = (fileAttributes & 0x0010) != 0;
        boolean archive = (fileAttributes & 0x0020) != 0;

        buffer.getShort();

        int modifiedTimestamp = buffer.getInt();
        int accessTimestamp = buffer.getInt();
        byte create10ms = buffer.get();
        byte modified10ms = buffer.get();
        byte createTZ = buffer.get();
        byte accessTZ = buffer.get();

        buffer.position(buffer.position() + 12);
    }
}
