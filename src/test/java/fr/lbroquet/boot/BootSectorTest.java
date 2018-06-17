package fr.lbroquet.boot;

import java.net.URL;
import org.junit.Assert;
import org.junit.Test;

public class BootSectorTest {

    @Test
    public void should_read_name() throws Exception {
        URL boot = BootSectorTest.class.getClassLoader().getResource("boot.bin");

        BootSector bootSector = BootSector.from(boot.getFile());

        Assert.assertEquals("EXFAT   ", bootSector.getName());
    }

    @Test
    public void should_have_correct_endianess() throws Exception {
        URL boot = BootSectorTest.class.getClassLoader().getResource("boot.bin");

        BootSector bootSector = BootSector.from(boot.getFile());

        Assert.assertEquals(0x22, bootSector.getPartitionOffset());
    }

    @Test
    public void should_convert_bytesPerSector() throws Exception {
        URL boot = BootSectorTest.class.getClassLoader().getResource("boot.bin");

        BootSector bootSector = BootSector.from(boot.getFile());

        Assert.assertEquals(Math.pow(2.0, bootSector.getBytesPerSectorExposant()), bootSector.getBytesPerSector(), 0.0);
    }

    @Test
    public void should_convert_sectorPerCluster() throws Exception {
        URL boot = BootSectorTest.class.getClassLoader().getResource("boot.bin");

        BootSector bootSector = BootSector.from(boot.getFile());

        Assert.assertEquals(Math.pow(2.0, bootSector.getSectorPerClusterExposant()), bootSector.getSectorPerCluster(), 0.0);
    }

    @Test
    public void should_convert_bytesPerCluster() throws Exception {
        URL boot = BootSectorTest.class.getClassLoader().getResource("boot.bin");

        BootSector bootSector = BootSector.from(boot.getFile());

        Assert.assertEquals(bootSector.getBytesPerSector() * bootSector.getSectorPerCluster(), bootSector.getBytesPerCluster());
    }

    @Test
    public void should_convert_volumeLengthInBytes() throws Exception {
        URL boot = BootSectorTest.class.getClassLoader().getResource("boot.bin");

        BootSector bootSector = BootSector.from(boot.getFile());

        Assert.assertEquals(bootSector.getBytesPerSector() * bootSector.getVolumeLength(), bootSector.getVolumeLengthInBytes());
    }
}
