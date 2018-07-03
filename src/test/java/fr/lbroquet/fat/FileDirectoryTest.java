package fr.lbroquet.fat;

import fr.lbroquet.fatchains.ResourceBuffer;
import java.nio.ByteBuffer;
import org.junit.Test;

public class FileDirectoryTest {

    @Test
    public void parseFileDirectory() throws Exception {
        ByteBuffer buffer = new ResourceBuffer("/filedirectory.bin").getBuffer();
        FileDirectory fileDirectory = new FileDirectory(buffer);
        fileDirectory.stream().forEach(System.out::println);
    }

}
