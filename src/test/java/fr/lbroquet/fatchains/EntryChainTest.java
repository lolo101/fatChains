package fr.lbroquet.fatchains;

import java.net.URL;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

public class EntryChainTest {

    @Test
    public void should_detect_unfinished() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/unfinished.bin");
        Stream<EntryChain> chains = new FatFile(fat).findHeads().chains();
        Assert.assertFalse(chains.findAny().map(EntryChain::isFinished).get());
    }

    @Test
    public void should_calculate_correct_length() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/unordered_singlechain.bin");
        Stream<EntryChain> chains = new FatFile(fat).findHeads().chains();
        Assert.assertEquals(3, chains.findAny().get().length());
    }

}
