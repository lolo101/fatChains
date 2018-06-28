package fr.lbroquet.fatchains;

import java.nio.charset.Charset;

public class EntryType {

    public static String searchSignature(byte[] array) {
        // Archives
        if (array[0] == 'P' && array[1] == 'K' && array[2] == 3 && array[3] == 4) {
            return "ZIP/JAR archive";
        }
        if (array[0] == '7' && array[1] == 'z' && array[2] == 0xffffffbc && array[3] == 0xffffffaf && array[4] == 0x27 && array[5] == 0x1c) {
            return "7z archive";
        }
        if (array[0] == 0x1f && array[1] == 0xffffff8b) {
            return "Gzip archive";
        }

        // Images
        if (array[0] == 0xffffff89 && array[1] == 'P' && array[2] == 'N' && array[3] == 'G' && array[4] == '\r' && array[5] == '\n' && array[6] == 0x1a && array[7] == 0x0a) {
            return "PNG image";
        }
        if (array[0] == -1 && array[1] == 0xffffffd8) {
            return "JPEG image";
        }

        // Videos
        if (array[0] == 'R' && array[1] == 'I' && array[2] == 'F' && array[3] == 'F') {
            return "RIFF (" + new String(array, 8, 4, Charset.forName("ASCII")) + ")";
        }
        if (array[0] == 'F' && array[1] == 'L' && array[2] == 'V' && array[3] == 1) {
            return "Macromedia Flash Video File (" + ((array[4] & 0x04) == 0 ? "" : "audio") + "/" + ((array[4] & 0x01) == 0 ? "" : "video") + ")";
        }
        if (array[4] == 'f' && array[5] == 't' && array[6] == 'y' && array[7] == 'p') {
            return "ISO Base Media File (brand:" + new String(array, 8, 4, Charset.forName("ASCII")) + ")";
        }

        // Texts
        if (array[0] == '<' && array[1] == '?' && array[2] == 'x' && array[3] == 'm' && array[4] == 'l') {
            return "XML document";
        }
        return showFirstBytes(array);
    }

    private static String showFirstBytes(byte[] array) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 32; ++i) {
            builder.append(String.format("%02x ", array[i]));
        }
        builder.append('\t');
        for (int i = 0; i < 32; ++i) {
            builder.append(asCharacter(array[i]));
        }
        return builder.toString();
    }

    private static char asCharacter(byte b) {
        return b >= 0x20 && b < 0x7f ? (char) b : '.';
    }
}
