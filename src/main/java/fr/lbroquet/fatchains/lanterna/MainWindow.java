package fr.lbroquet.fatchains.lanterna;

import com.googlecode.lanterna.gui2.AbstractWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.WaitingDialog;
import fr.lbroquet.fatchains.Partition;
import java.io.IOException;
import java.util.function.Supplier;

class MainWindow extends AbstractWindow {

    private static final System.Logger LOG = System.getLogger(MainWindow.class.getName());

    private final Panel mainPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
    private final Partition partition;

    MainWindow(Partition partition) {
        super("Fat Chains");
        this.partition = partition;

        Panel menuPanel = new Panel();
        menuPanel.addComponent(new Button("Boot Sector", () -> waitShow(this::bootSector)));
        menuPanel.addComponent(new Button("FAT", () -> waitShow(this::fat)));

        mainPanel.addComponent(menuPanel);

        Panel panel = new Panel();
        panel.addComponent(mainPanel);
        panel.addComponent(new Button("Quit", this::close), LinearLayout.createLayoutData(LinearLayout.Alignment.End));
        setComponent(panel);
    }

    private void waitShow(Supplier<Component> supplier) {
        WindowBasedTextGUI gui = getTextGUI();
        WaitingDialog dialog = WaitingDialog.showDialog(gui, partition.getFileName(), "Reading boot sector\nPlease wait...");
        try {
            gui.updateScreen();
            Component c = supplier.get();
            mainPanel.addComponent(c);
        } catch (IOException ex) {
            LOG.log(System.Logger.Level.ERROR, "", ex);
        } finally {
            dialog.close();
        }
    }

    private Component bootSector() {
        BootSectorPanel bootSectorPanel = new BootSectorPanel();
        bootSectorPanel.init(partition.getBootSector());
        return bootSectorPanel;
    }

    private Component fat() {
        FatPanel fatPanel = new FatPanel(partition);
        fatPanel.init();
        return fatPanel;
    }
}
