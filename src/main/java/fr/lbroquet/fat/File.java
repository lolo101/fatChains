package fr.lbroquet.fat;

import java.nio.ByteBuffer;
import java.util.stream.Stream;

public class File {

    private final String fileName;

    File(ByteBuffer buffer) {
        FileDirectoryEntry fileDirectoryEntry = new FileDirectoryEntry(buffer);
        StreamExtension streamExtension = new StreamExtension(buffer);

        this.fileName = readFileName(buffer,
                fileDirectoryEntry.getSecondaryCount() - 1,
                streamExtension.getNameLength());
    }

    @Override
    public String toString() {
        return fileName;
    }

    private String readFileName(ByteBuffer buffer, int count, int nameLength) {
        return Stream.generate(() -> buffer)
                .limit(count)
                .map(FileNameDirectoryEntry::new)
                .map(FileNameDirectoryEntry::getFileNameFragment)
                .reduce(String::concat)
                .map(s -> s.substring(0, nameLength))
                .get();
    }
}
