package fr.lbroquet.fat;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileDirectory {

    private final List<File> files = new ArrayList<>();

    public FileDirectory(ByteBuffer buffer) {
        while (buffer.hasRemaining() && buffer.get(buffer.position()) != 0) {
            files.add(readSingleFile(buffer.order(ByteOrder.LITTLE_ENDIAN)));
        }
    }

    private File readSingleFile(ByteBuffer buffer) {
        FileDirectoryEntry fileDirectoryEntry = new FileDirectoryEntry(buffer);
        StreamExtension streamExtension = new StreamExtension(buffer);

        String fileName = readFileName(buffer,
                fileDirectoryEntry.getSecondaryCount() - 1,
                streamExtension.getNameLength());

        return new File(fileName,
                streamExtension.getFirstCluster(),
                streamExtension.getValidDataLength());
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

    public Stream<File> stream() {
        return files.stream();
    }
}
