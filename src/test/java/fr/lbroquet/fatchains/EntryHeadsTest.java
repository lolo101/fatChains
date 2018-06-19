package fr.lbroquet.fatchains;

import java.net.URL;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

public class EntryHeadsTest {

    @Test
    public void should_ignore_loop() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/loop.bin");
        Collection<EntryChain> chains = new FatFile(fat).findChains();
        Assert.assertTrue(chains.isEmpty());
    }

    @Test
    public void should_have_single_chain() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/unordered_singlechain.bin");
        Collection<EntryChain> chains = new FatFile(fat).findChains();
        Assert.assertEquals(1, chains.size());
    }

    @Test
    public void should_have_all_entries_in_chain() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/unordered_singlechain2.bin");
        Collection<EntryChain> chains = new FatFile(fat).findChains();
        EntryChain chain = chains.iterator().next();
        Assert.assertEquals(4, chain.stream().count());
    }

    @Test
    public void multichain() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/pointer_to_middle.bin");
        Collection<EntryChain> chains = new FatFile(fat).findChains();
        Assert.assertEquals(2, chains.size());
    }

    @Test
    public void interrupted() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/interrupted_chain.bin");
        Collection<EntryChain> chains = new FatFile(fat).findChains();
        Assert.assertEquals(2, chains.size());
    }
}
