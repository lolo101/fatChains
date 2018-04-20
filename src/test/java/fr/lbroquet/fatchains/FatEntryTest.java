package fr.lbroquet.fatchains;

import org.junit.Assert;
import org.junit.Test;

public class FatEntryTest {

    @Test
    public void shoud_compute_next_index_correctly() {
        FatEntry instance = new FatEntry(0, new byte[] {1, 2, 3, 4});
        Assert.assertEquals(0x04030201, instance.getNextEntryIndex());
    }

}
