package fr.lbroquet.fat;

import java.nio.ByteBuffer;
import java.util.stream.Stream;

public class File {

    private final String fileName;
    private final FileDirectoryEntry fileDirectoryEntry;
    private final StreamExtension streamExtension;

    File(ByteBuffer buffer) {
        this.fileDirectoryEntry = new FileDirectoryEntry(buffer);
        this.streamExtension = new StreamExtension(buffer);
        this.fileName = readFileName(buffer);
    }

    @Override
    public String toString() {
        return fileName;
    }

    private String readFileName(ByteBuffer buffer) {
        int count = fileDirectoryEntry.countFileNameEntries();
        int nameLength = streamExtension.getNameLength();
        return Stream.generate(() -> buffer)
                .limit(count)
                .map(FileNameDirectoryEntry::new)
                .map(FileNameDirectoryEntry::getFileNameFragment)
                .reduce(String::concat)
                .map(s -> s.substring(0, nameLength))
                .get();
    }
}
