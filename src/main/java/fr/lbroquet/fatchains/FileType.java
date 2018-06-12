package fr.lbroquet.fatchains;

import java.io.IOException;
import java.io.InputStream;

public class FileType {

    private static final int HEAP_OFFSET_IN_SECTORS = 61440;
	private static final int SECTORS_PER_CLUSTER = 256;
    private static final int BYTES_PER_SECTOR = 512;

    public static String guessClusterType(int headIndex, InputStream device) throws IOException {
        int clusterOffset = HEAP_OFFSET_IN_SECTORS + (headIndex - 2) * SECTORS_PER_CLUSTER;
        device.skip(clusterOffset * BYTES_PER_SECTOR);

        byte[] begining = new byte[32];
        device.read(begining);
        return guessType(begining);
    }

    private static String guessType(byte[] array) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 32; ++i) {
            builder.append(Integer.toHexString(array[i] & 0xff)).append(' ');
        }
        builder.append('\t');
        for (int i = 0; i < 32; ++i) {
            builder.append(asCharacter(array[i]));
        }
        builder.append("\t\u001b[31m(")
                .append(searchSignature(array))
                .append("\u001b[0m");
        return builder.toString();
    }

    private static char asCharacter(byte b) {
        return b >= 0x20 && b < 0x7f ? (char) b : '.';
    }

    private static String searchSignature(byte[] array) {
        if (array[0] == 'P' && array[1] == 'K' && array[2] == 3 && array[3] == 4) {
            return "ZIP/JAR archive";
        }
        if (array[0] == '7' && array[1] == 'z' && array[2] == 0xffffffbc && array[3] == 0xffffffaf && array[4] == 0x27 && array[5] == 0x1c) {
            return "7z archive";
        }
        if (array[0] == 0x1f && array[1] == 0xffffff8b) {
            return "Gzip archive";
        }
        if (array[0] == 0xffffff89 && array[1] == 'P' && array[2] == 'N' && array[3] == 'G' && array[4] == '\r' && array[5] == '\n' && array[6] == 0x1a && array[7] == 0x0a) {
            return "PNG image";
        }
        if (array[0] == -1 && array[1] == 0xffffffd8 && array[2] == -1 && array[3] == 0xffffffe0) {
            return "JPEG image";
        }
        if (array[0] == '<' && array[1] == '?' && array[2] == 'x' && array[3] == 'm' && array[4] == 'l') {
            return "XML document";
        }
        return "?";
    }
}
