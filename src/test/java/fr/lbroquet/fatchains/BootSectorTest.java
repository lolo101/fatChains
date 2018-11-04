package fr.lbroquet.fatchains;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import org.junit.Assert;
import org.junit.Test;

public class BootSectorTest {

    private final BootSector bootSector;

    public BootSectorTest() throws URISyntaxException, IOException {
        URL boot = BootSectorTest.class.getClassLoader().getResource("boot.bin");
        try (Partition partition = new Partition(Paths.get(boot.toURI()))) {
            bootSector = partition.getBootSector();
        }
    }

    @Test
    public void should_read_name() throws Exception {
        Assert.assertEquals("EXFAT   ", bootSector.getName());
    }

    @Test
    public void should_have_correct_endianess() throws Exception {
        Assert.assertEquals(0x22, bootSector.getPartitionOffset());
    }

    @Test
    public void should_convert_bytesPerSector() throws Exception {
        Assert.assertEquals(Math.pow(2.0, bootSector.getBytesPerSectorExposant()), bootSector.getBytesPerSector(), 0.0);
    }

    @Test
    public void should_convert_sectorPerCluster() throws Exception {
        Assert.assertEquals(Math.pow(2.0, bootSector.getSectorPerClusterExposant()), bootSector.getSectorPerCluster(), 0.0);
    }

    @Test
    public void should_convert_bytesPerCluster() throws Exception {
        Assert.assertEquals(bootSector.getBytesPerSector() * bootSector.getSectorPerCluster(), bootSector.getBytesPerCluster());
    }

    @Test
    public void should_convert_volumeLengthInBytes() throws Exception {
        Assert.assertEquals(bootSector.getBytesPerSector() * bootSector.getVolumeLength(), bootSector.getVolumeLengthInBytes());
    }

    @Test
    public void should_convert_fatOffsetInBytes() throws Exception {
        Assert.assertEquals(bootSector.getBytesPerSector() * bootSector.getFatOffset(), bootSector.getFatOffsetInBytes());
    }

    @Test
    public void should_convert_fatLengthInBytes() throws Exception {
        Assert.assertEquals(bootSector.getBytesPerSector() * bootSector.getFatLength(), bootSector.getFatLengthInBytes());
    }

    @Test
    public void should_convert_clusterOffsetInBytes() throws Exception {
        Assert.assertEquals(Math.multiplyExact(bootSector.getBytesPerSector(), bootSector.getClusterOffset()), bootSector.getClusterOffsetInBytes());
    }

    @Test
    public void should_calculate_clusterPositionInBytes() throws Exception {
        for (int clusterIndex = 0; clusterIndex < 10; ++clusterIndex) {
            FatEntry entry = new FatEntry(clusterIndex + 2, FatEntry.END_OF_CHAIN);
            Assert.assertEquals(bootSector.getClusterOffsetInBytes() + clusterIndex * bootSector.getBytesPerCluster(), bootSector.getClusterPosition(entry));
        }
    }
}
