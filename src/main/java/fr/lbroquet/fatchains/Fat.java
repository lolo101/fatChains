package fr.lbroquet.fatchains;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.stream.StreamSupport;

public class Fat {

    static Collection<EntryChain> findChains(InputStream input) throws IOException {
        try(FatEntryInputStream channel = new FatEntryInputStream(input)) {
            channel.nextEntry(); // 0xfffffff8 Media type entry
            channel.nextEntry(); // END_OF_CHAIN
            EntryHeads heads = Fat.findChains(channel);
            return heads.chains();
        }
    }

    private static EntryHeads findChains(FatEntryInputStream channel) throws IOException {
        return StreamSupport.stream(channel.spliterator(), false)
                .filter(FatEntry::isAllocated)
                .reduce(new EntryHeads(), EntryHeads::consider, EntryHeads::merge);
    }
}
