package fr.lbroquet.fatchains;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.SortedMap;

class EntryChain {

    private final Deque<FatEntry> queue = new ArrayDeque<>();

    EntryChain(int head, SortedMap<Integer, FatEntry> allocateds) {
        for (FatEntry entry = allocateds.get(head); entry != null; entry = allocateds.get(entry.getNextEntryIndex())) {
            queue.addLast(entry);
        }
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
