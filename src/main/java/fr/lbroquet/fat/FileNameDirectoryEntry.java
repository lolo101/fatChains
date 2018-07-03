package fr.lbroquet.fat;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import lombok.Value;

@Value
public class FileNameDirectoryEntry {

    private final String fileNameFragment;

    public FileNameDirectoryEntry(ByteBuffer buffer) {
        byte tag = buffer.get();
        if (tag != 0xffffffc1) {
            throw new IllegalArgumentException(String.format("Invalid tag: %02x", tag));
        }
        buffer.get();

        byte[] fileNameArray = new byte[30];
        buffer.get(fileNameArray);
        fileNameFragment = new String(fileNameArray, Charset.forName("UTF-16LE"));
    }

}
