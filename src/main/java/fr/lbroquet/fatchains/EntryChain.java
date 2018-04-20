package fr.lbroquet.fatchains;

import java.util.ArrayDeque;
import java.util.Deque;

class EntryChain {

    private final Deque<FatEntry> queue = new ArrayDeque<>();

    public void push(FatEntry entry) {
        queue.addLast(entry);
    }

    public void push(EntryChain chain) {
        queue.addAll(chain.queue);
    }

    public int size() {
        return queue.size();
    }

    public int firstIndex() {
        return queue.getFirst().getIndex();
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
