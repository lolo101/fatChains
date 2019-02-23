package fr.lbroquet.fat;

import fr.lbroquet.fatchains.EntryChain;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class FileDirectory implements Iterable<File> {

    private final EntryChain entryChain;

    public FileDirectory(EntryChain chain) {
        this.entryChain = chain;
    }

    @Override
    public Iterator<File> iterator() {
        return new Iterator<File>() {

            private final Iterator<ByteBuffer> internal = entryChain.iterator();
            private final List<File> files = new ArrayList<>();

            @Override
            public boolean hasNext() {
                return !files.isEmpty() || (internal.hasNext() && files.addAll(nextBatchOfFiles()));
            }

            @Override
            public File next() {
                return files.remove(0);
            }

            private Collection<File> nextBatchOfFiles() {
                List<File> batch = new ArrayList<>();
                for (ByteBuffer buffer = internal.next(); buffer.hasRemaining() && buffer.get(buffer.position()) != 0;) {
                    batch.add(new File(buffer.order(ByteOrder.LITTLE_ENDIAN)));
                }
                return batch;
            }
        };
    }
}
