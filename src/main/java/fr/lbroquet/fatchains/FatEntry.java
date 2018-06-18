package fr.lbroquet.fatchains;

public class FatEntry implements Comparable<FatEntry> {

    public static final int END_OF_CHAIN = 0xffffffff;
    private final int index;
    private final int nextEntryIndex;

    FatEntry(int index, int nextEntryIndex) {
        this.index = index;
        this.nextEntryIndex = nextEntryIndex;
    }

    public int getIndex() {
        return index;
    }

    public int getNextEntryIndex() {
        return nextEntryIndex;
    }

    public boolean isLastOfChain() {
        return nextEntryIndex == END_OF_CHAIN;
    }

    @Override
    public int compareTo(FatEntry other) {
        return Integer.compare(index, other.index);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(index);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FatEntry && compareTo((FatEntry) obj) == 0;
    }

}
