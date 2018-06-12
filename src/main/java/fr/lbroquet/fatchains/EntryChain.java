package fr.lbroquet.fatchains;

import java.util.ArrayDeque;
import java.util.Deque;

class EntryChain {

    private final Deque<FatEntry> queue = new ArrayDeque<>();

    public EntryChain append(FatEntry entry) {
        queue.addLast(entry);
        return this;
    }

    public int size() {
        return queue.size();
    }

    public int firstIndex() {
        return queue.getFirst().getIndex();
    }

    public boolean isFinished() {
        return queue.getLast().isLastOfChain();
    }

    @Override
    public String toString() {
        return String.format("%08x .. %08x (-> %08x)\tsize: %dkB",
                firstIndex(),
                queue.getLast().getIndex(),
                queue.getLast().getNextEntryIndex(),
                queue.size() * 128);
    }
}
