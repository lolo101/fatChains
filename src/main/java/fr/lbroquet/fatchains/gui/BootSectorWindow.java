package fr.lbroquet.fatchains.gui;

import com.googlecode.lanterna.gui2.AbstractWindow;
import com.googlecode.lanterna.gui2.Border;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import fr.lbroquet.boot.BootSector;
import java.nio.charset.Charset;

class BootSectorWindow extends AbstractWindow {

    private final Panel mainPanel = new Panel();

    BootSectorWindow(BootSector bootSector) {
        super("Boot Sector");
        Panel boot = new Panel();
        Border border = Borders.singleLine();
        border.addTo(boot);

        byte[] jmp = bootSector.getJmp();
        boot.addComponent(new Label(String.format("JMP %02x %02x %02x", jmp[0], jmp[1], jmp[2])));
        boot.addComponent(new Label("name: " + new String(bootSector.getName(), Charset.forName("ASCII"))));
        boot.addComponent(new EmptySpace());
        boot.addComponent(new Label(String.format("SN %08x)", bootSector.getVolumeSN())));
        byte[] fsRev = bootSector.getFsRev();
        boot.addComponent(new Label(String.format("FS rev. %02x.%02x", fsRev[0], fsRev[1])));
        boot.addComponent(new EmptySpace());
        boot.addComponent(new Label(String.format("Bytes Per Sector  : 2^%d (%d)", bootSector.getBytesPerSectorExposant(), bootSector.getBytesPerSector())));
        boot.addComponent(new Label(String.format("Sector Per Cluster: 2^%d (%d)", bootSector.getSectorPerClusterExposant(), bootSector.getSectorPerCluster())));
        boot.addComponent(new Label(String.format("(Cluster size     : %d)", bootSector.getBytesPerCluster())));
        boot.addComponent(new EmptySpace());
        boot.addComponent(new Label("Partition offset: " + bootSector.getPartitionOffset()));
        boot.addComponent(new Label("Volume length   : " + bootSector.getVolumeLength()));
        boot.addComponent(new Label(String.format("Usage: %x%%", bootSector.getPctUse())));
        boot.addComponent(new EmptySpace());
        boot.addComponent(new Label("FAT offset: " + bootSector.getFatOffset()));
        boot.addComponent(new Label("FAT length: " + bootSector.getFatLength()));
        boot.addComponent(new Label("Nb of FATs: " + bootSector.getNbFats()));
        boot.addComponent(new EmptySpace());
        boot.addComponent(new Label("Cluster heap offset: " + bootSector.getClusterOffset()));
        boot.addComponent(new Label("Cluster count      : " + bootSector.getClusterCount()));
        boot.addComponent(new Label("Root cluster       : " + bootSector.getRootCluster()));
        boot.addComponent(new EmptySpace());
        boot.addComponent(new Label("Flags: " + bootSector.getFlags()));
        boot.addComponent(new Label("\tActive FAT: " + ((bootSector.getFlags() & 1) == 0 ? "First" : "Second")));
        boot.addComponent(new Label("\tDirtiness : " + ((bootSector.getFlags() & 2) == 0 ? "Clean" : "Dirty")));
        boot.addComponent(new Label("\tFailure   : " + ((bootSector.getFlags() & 4) == 0 ? "No" : "Yes")));
        boot.addComponent(new EmptySpace());
        boot.addComponent(new Label(String.format("Drive Select: %02x", bootSector.getDriveSelect())));

        mainPanel.addComponent(new Button("Quit", this::close));
        setComponent(mainPanel);
    }

}
