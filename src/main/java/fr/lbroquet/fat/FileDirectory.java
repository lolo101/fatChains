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
            files.add(new File(buffer.order(ByteOrder.LITTLE_ENDIAN)));
        }
    }

    public Stream<File> stream() {
        return files.stream();
    }
}
