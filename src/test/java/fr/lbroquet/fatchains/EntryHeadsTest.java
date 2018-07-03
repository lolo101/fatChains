package fr.lbroquet.fatchains;

import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

public class EntryHeadsTest {

    @Test
    public void should_ignore_loop() throws Exception {
        Stream<EntryChain> chains = new FatFile("/loop.bin").readChains();
        Assert.assertEquals(0, chains.count());
    }

    @Test
    public void should_have_single_chain() throws Exception {
        Stream<EntryChain> chains = new FatFile("/unordered_singlechain.bin").readChains();
        Assert.assertEquals(1, chains.count());
    }

    @Test
    public void should_have_all_entries_in_chain() throws Exception {
        EntryChain chain = new FatFile("/unordered_singlechain2.bin").readChains().findAny().get();
        Assert.assertEquals(4, chain.length());
    }

    @Test
    public void multichain() throws Exception {
        Stream<EntryChain> chains = new FatFile("/pointer_to_middle.bin").readChains();
        Assert.assertEquals(2, chains.count());
    }

    @Test
    public void interrupted() throws Exception {
        Stream<EntryChain> chains = new FatFile("/interrupted_chain.bin").readChains();
        Assert.assertEquals(2, chains.count());
    }
}
