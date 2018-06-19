package fr.lbroquet.fatchains;

import java.net.URL;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

public class EntryChainTest {

    @Test
    public void should_detect_unfinished() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/unfinished.bin");
        Collection<EntryChain> chains = new FatFile(fat).findChains();
        Assert.assertFalse(chains.iterator().next().isFinished());
    }

    @Test
    public void should_calculate_correct_length() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/unordered_singlechain.bin");
        Collection<EntryChain> chains = new FatFile(fat).findChains();
        Assert.assertEquals(3, chains.iterator().next().length());
    }

}
