package fr.lbroquet.fatchains;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class MainTest {

    @Test(expected = IOException.class)
    @Ignore("Loops not detected anymore")
    public void should_throw_on_loop() throws Exception {
        InputStream fat = MainTest.class.getResourceAsStream("/loop.bin");
        Main.findChains(fat);
    }

    @Test(expected = IOException.class)
    @Ignore("Just warn and continue")
    public void should_throw_on_unfinished() throws Exception {
        InputStream fat = MainTest.class.getResourceAsStream("/unfinished.bin");
        Main.findChains(fat);
    }

    @Test
    public void should_have_single_chain() throws Exception {
        InputStream fat = MainTest.class.getResourceAsStream("/unordered_singlechain.bin");
        Collection<EntryChain> chains = Main.findChains(fat);
        Assert.assertEquals(1, chains.size());
    }

    @Test
    public void should_have_all_entries_in_chain() throws Exception {
        InputStream fat = MainTest.class.getResourceAsStream("/unordered_singlechain2.bin");
        Collection<EntryChain> chains = Main.findChains(fat);
        EntryChain chain = chains.iterator().next();
        Assert.assertEquals(4, chain.size());
    }

    @Test
    public void multichain() throws Exception {
        InputStream fat = MainTest.class.getResourceAsStream("/pointer_to_middle.bin");
        Collection<EntryChain> chains = Main.findChains(fat);
        Assert.assertEquals(2, chains.size());
    }

    @Test
    public void interrupted() throws Exception {
        InputStream fat = MainTest.class.getResourceAsStream("/interrupted_chain.bin");
        Collection<EntryChain> chains = Main.findChains(fat);
        Assert.assertEquals(2, chains.size());
    }

}
