package fr.lbroquet.fatchains;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class EntryHeadsTest {

    @Test
    public void should_ignore_loop() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/loop.bin");
        Collection<EntryChain> chains = findChains(fat);
        Assert.assertTrue(chains.isEmpty());
    }

    @Test(expected = IOException.class)
    @Ignore("Just warn and continue")
    public void should_throw_on_unfinished() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/unfinished.bin");
        findChains(fat);
    }

    @Test
    public void should_have_single_chain() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/unordered_singlechain.bin");
        Collection<EntryChain> chains = findChains(fat);
        Assert.assertEquals(1, chains.size());
    }

    @Test
    public void should_have_all_entries_in_chain() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/unordered_singlechain2.bin");
        Collection<EntryChain> chains = findChains(fat);
        EntryChain chain = chains.iterator().next();
        Assert.assertEquals(4, chain.stream().count());
    }

    @Test
    public void multichain() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/pointer_to_middle.bin");
        Collection<EntryChain> chains = findChains(fat);
        Assert.assertEquals(2, chains.size());
    }

    @Test
    public void interrupted() throws Exception {
        URL fat = EntryHeadsTest.class.getResource("/interrupted_chain.bin");
        Collection<EntryChain> chains = findChains(fat);
        Assert.assertEquals(2, chains.size());
    }

    private static Collection<EntryChain> findChains(URL input) throws URISyntaxException, IOException {
        Path path = Paths.get(input.toURI());
        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            int fileSize = (int) Files.size(path);
            ByteBuffer fileBuffer = ByteBuffer.allocate(fileSize);
            channel.read(fileBuffer);
            FatEntries entries = new FatEntries(fileBuffer.rewind());
            return EntryHeads.from(entries).chains();
        }
    }
}
