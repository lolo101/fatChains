package fr.lbroquet.fat;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import org.junit.Assert;
import org.junit.Test;

public class FileNameDirectoryEntryTest {

    private static final String FILE_NAME = "File-Name";

    @Test
    public void should_use_correct_encoding() {
        byte[] fileNameArray = FILE_NAME.getBytes(Charset.forName("UTF-16LE"));
        byte[] array = new byte[32];
        array[0] = 0xffffffc1;
        System.arraycopy(fileNameArray, 0, array, 2, fileNameArray.length);
        ByteBuffer buffer = ByteBuffer.wrap(array);

        FileNameDirectoryEntry fileNameDirectoryEntry = new FileNameDirectoryEntry(buffer);

        Assert.assertEquals(FILE_NAME, fileNameDirectoryEntry.getFileNameFragment().trim());
    }
}
