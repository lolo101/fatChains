package fr.lbroquet.fatchains.gui;

import com.googlecode.lanterna.gui2.AbstractWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.WaitingDialog;
import fr.lbroquet.fatchains.Partition;
import java.io.IOException;

class MainWindow extends AbstractWindow {

    private static final System.Logger LOG = System.getLogger(MainWindow.class.getName());

    private final Panel mainPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
    private final Partition partition;

    MainWindow(Partition partition) {
        super("Fat Chains");
        this.partition = partition;

        Panel menuPanel = new Panel();
        menuPanel.addComponent(new Button("Boot Sector", this::scanBootSector));
        menuPanel.addComponent(new Button("FAT", this::scanFat));

        mainPanel.addComponent(menuPanel);

        Panel panel = new Panel();
        panel.addComponent(mainPanel);
        panel.addComponent(new Button("Quit", this::close), LinearLayout.createLayoutData(LinearLayout.Alignment.End));
        setComponent(panel);
    }

    private void scanBootSector() {
        try {
            WindowBasedTextGUI gui = getTextGUI();
            WaitingDialog dialog = WaitingDialog.showDialog(gui, partition.getFileName(), "Reading boot sector\nPlease wait...");
            gui.updateScreen();
            showBootSector();
            dialog.close();
        } catch (IOException ex) {
            LOG.log(System.Logger.Level.ERROR, "", ex);
        }
    }

    private void showBootSector() throws IOException {
        BootSectorPanel bootSectorPanel = new BootSectorPanel();
        bootSectorPanel.init(partition.getBootSector());
        mainPanel.addComponent(bootSectorPanel);
    }

    private void scanFat() {
        try {
            WindowBasedTextGUI gui = getTextGUI();
            WaitingDialog dialog = WaitingDialog.showDialog(gui, partition.getFileName(), "Reading FAT\nPlease wait...");
            gui.updateScreen();
            showFat();
            dialog.close();
        } catch (IOException ex) {
            LOG.log(System.Logger.Level.ERROR, "", ex);
        }
    }

    private void showFat() throws IOException {
        FatPanel fatPanel = new FatPanel(partition);
        fatPanel.init();
        mainPanel.addComponent(fatPanel);
    }
}
