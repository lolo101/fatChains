package fr.lbroquet.fatchains;

import java.net.URL;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

public class EntryHeadsTest {

    @Test
    public void should_ignore_loop() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/loop.bin");
        Stream<EntryChain> chains = new FatFile(fat).findHeads().chains();
        Assert.assertEquals(0, chains.count());
    }

    @Test
    public void should_have_single_chain() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/unordered_singlechain.bin");
        Stream<EntryChain> chains = new FatFile(fat).findHeads().chains();
        Assert.assertEquals(1, chains.count());
    }

    @Test
    public void should_have_all_entries_in_chain() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/unordered_singlechain2.bin");
        EntryChain chain = new FatFile(fat).findHeads().chains().findAny().get();
        Assert.assertEquals(4, chain.length());
    }

    @Test
    public void multichain() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/pointer_to_middle.bin");
        Stream<EntryChain> chains = new FatFile(fat).findHeads().chains();
        Assert.assertEquals(2, chains.count());
    }

    @Test
    public void interrupted() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/interrupted_chain.bin");
        Stream<EntryChain> chains = new FatFile(fat).findHeads().chains();
        Assert.assertEquals(2, chains.count());
    }
}
