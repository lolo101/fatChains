package fr.lbroquet.fat;

public class File {

    private final String fileName;

    File(String fileName, int clusterIndex, long length) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return fileName;
    }
}
