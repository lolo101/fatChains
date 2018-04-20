package fr.lbroquet.fatchains;

class FatEntry implements Comparable<FatEntry> {

    public static final int END_OF_CHAIN = 0xffffffff;
    private final int index;
    private final int value;

    FatEntry(int index, byte[] b) {
        this.index = index;
        this.value = ((b[3] & 0xff) << 24)
                + ((b[2]  &0xff) << 16)
                + ((b[1] & 0xff) << 8)
                + (b[0] & 0xff);
    }

    public int getIndex() {
        return index;
    }

    public int getNextEntryIndex() {
        return value;
    }

    public boolean isLastOfChain() {
        return value == END_OF_CHAIN;
    }

    public boolean isAllocated() {
        return value != 0;
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
