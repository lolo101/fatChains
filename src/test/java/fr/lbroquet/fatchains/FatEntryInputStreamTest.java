package fr.lbroquet.fatchains;

import java.io.InputStream;
import org.junit.Assert;
import org.junit.Test;

public class FatEntryInputStreamTest {

    @Test
    public void first_entry_should_be_valid_mediatype() throws Exception {
        InputStream input = FatEntryInputStreamTest.class.getResourceAsStream("/fat.bin");
        FatEntryInputStream instance = new FatEntryInputStream(input);
        FatEntry fatEntry0 = instance.nextEntry();
        Assert.assertEquals(0xfffffff8, fatEntry0.getNextEntryIndex());
    }

    @Test
    public void second_entry_should_be_end_of_chain() throws Exception {
        InputStream input = FatEntryInputStreamTest.class.getResourceAsStream("/fat.bin");
        FatEntryInputStream instance = new FatEntryInputStream(input);
        instance.nextEntry(); // skip entry 0
        FatEntry fatEntry1 = instance.nextEntry();
        Assert.assertEquals(FatEntry.END_OF_CHAIN, fatEntry1.getNextEntryIndex());
    }

}
