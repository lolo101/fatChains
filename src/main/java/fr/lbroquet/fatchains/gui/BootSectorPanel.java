package fr.lbroquet.fatchains.gui;

import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import fr.lbroquet.fatchains.BootSector;

class BootSectorPanel extends Panel {

    public void init(BootSector bootSector) {
        byte[] jmp = bootSector.getJmp();
        addComponent(new Label(String.format("JMP %02x %02x %02x", jmp[0], jmp[1], jmp[2])));
        addComponent(new Label("name: " + bootSector.getName()));
        addComponent(new EmptySpace());
        addComponent(new Label(String.format("SN %08x", bootSector.getVolumeSN())));
        byte[] fsRev = bootSector.getFsRev();
        addComponent(new Label(String.format("FS rev. %02x.%02x", fsRev[1], fsRev[0])));
        addComponent(new EmptySpace());
        addComponent(new Label(String.format("Bytes per Sector  : 2^%d (%d)", bootSector.getBytesPerSectorExposant(), bootSector.getBytesPerSector())));
        addComponent(new Label(String.format("Sector per Cluster: 2^%d (%d)", bootSector.getSectorPerClusterExposant(), bootSector.getSectorPerCluster())));
        addComponent(new Label(String.format("(Bytes per Cluster: %d)", bootSector.getBytesPerCluster())));
        addComponent(new EmptySpace());
        addComponent(new Label("Partition offset: " + bootSector.getPartitionOffset() + " sectors"));
        addComponent(new Label("Volume length   : " + bootSector.getVolumeLength() + " sectors"));
        addComponent(new Label("(" + bootSector.getVolumeLengthInBytes() + " Bytes)"));
        addComponent(new Label(String.format("Usage: %d%%", bootSector.getPctUse())));
        addComponent(new EmptySpace());
        addComponent(new Label("FAT offset: " + bootSector.getFatOffset() + " sectors"));
        addComponent(new Label("FAT length: " + bootSector.getFatLength() + " sectors"));
        addComponent(new Label("Nb of FATs: " + bootSector.getNbFats()));
        addComponent(new EmptySpace());
        addComponent(new Label("Cluster heap offset: " + bootSector.getClusterOffset() + " sectors"));
        addComponent(new Label("Cluster count      : " + bootSector.getClusterCount()));
        addComponent(new Label("Root cluster       : " + bootSector.getRootCluster()));
        addComponent(new EmptySpace());
        addComponent(new Label("Flags: " + bootSector.getFlags()));
        addComponent(new Label("    Active FAT: " + ((bootSector.getFlags() & 1) == 0 ? "First" : "Second")));
        addComponent(new Label("    Dirtiness : " + ((bootSector.getFlags() & 2) == 0 ? "Clean" : "Dirty")));
        addComponent(new Label("    Failure   : " + ((bootSector.getFlags() & 4) == 0 ? "No" : "Yes")));
        addComponent(new EmptySpace());
        addComponent(new Label(String.format("Drive Select: %02x", bootSector.getDriveSelect())));
    }

}
