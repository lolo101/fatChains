package fr.lbroquet.fatchains;

public class EntryType {

    public static String searchSignature(byte[] array) {
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
