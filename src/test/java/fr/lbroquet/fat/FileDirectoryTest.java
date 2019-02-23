package fr.lbroquet.fat;

import static java.util.Collections.singleton;
import static org.mockito.Mockito.mock;

import fr.lbroquet.fatchains.EntryChain;
import fr.lbroquet.fatchains.ResourceBuffer;
import java.nio.ByteBuffer;
import org.junit.Test;
import org.mockito.Mockito;

public class FileDirectoryTest {

    @Test
    public void parseFileDirectory() throws Exception {
        ByteBuffer buffer = new ResourceBuffer("/filedirectory.bin").getBuffer();
        EntryChain chain = mock(EntryChain.class);
        Mockito.when(chain.iterator()).thenReturn(singleton(buffer).iterator());

        FileDirectory fileDirectory = new FileDirectory(chain);
        fileDirectory.forEach(System.out::println);
    }

}
