package fr.lbroquet.fatchains;

import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

public class EntryChainTest {

    @Test
    public void should_detect_unfinished() throws Exception {
        Stream<EntryChain> chains = new FatFile("/unfinished.bin").readChains();
        Assert.assertFalse(chains.findAny().map(EntryChain::isFinished).get());
    }

    @Test
    public void should_calculate_correct_length() throws Exception {
        Stream<EntryChain> chains = new FatFile("/unordered_singlechain.bin").readChains();
        Assert.assertEquals(3, chains.findAny().get().length());
    }

}
