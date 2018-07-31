package fr.lbroquet.fatchains;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class EntryHeadsTest {

    @Test
    public void should_ignore_loop() throws Exception {
        List<EntryChain> chains = new FatFile("/loop.bin").readChains();
        Assert.assertEquals(0, chains.size());
    }

    @Test
    public void should_have_single_chain() throws Exception {
        List<EntryChain> chains = new FatFile("/unordered_singlechain.bin").readChains();
        Assert.assertEquals(1, chains.size());
    }

    @Test
    public void should_have_all_entries_in_chain() throws Exception {
        EntryChain chain = new FatFile("/unordered_singlechain2.bin").readChains().get(0);
        Assert.assertEquals(4, chain.length());
    }

    @Test
    public void multichain() throws Exception {
        List<EntryChain> chains = new FatFile("/pointer_to_middle.bin").readChains();
        Assert.assertEquals(2, chains.size());
    }

    @Test
    public void interrupted() throws Exception {
        List<EntryChain> chains = new FatFile("/interrupted_chain.bin").readChains();
        Assert.assertEquals(2, chains.size());
    }
}
