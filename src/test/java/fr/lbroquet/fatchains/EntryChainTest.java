package fr.lbroquet.fatchains;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class EntryChainTest {

    @Test
    public void should_detect_unfinished() throws Exception {
        List<EntryChain> chains = new FatFile("/unfinished.bin").readChains();
        Assert.assertFalse(chains.get(0).isFinished());
    }

    @Test
    public void should_calculate_correct_length() throws Exception {
        List<EntryChain> chains = new FatFile("/unordered_singlechain.bin").readChains();
        Assert.assertEquals(3, chains.get(0).length());
    }

}
