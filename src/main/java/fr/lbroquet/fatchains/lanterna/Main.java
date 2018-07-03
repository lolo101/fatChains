package fr.lbroquet.fatchains.lanterna;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.dialogs.FileDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import fr.lbroquet.fatchains.Partition;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Main {

    private static final System.Logger LOG = System.getLogger(Main.class.getName());

    private final MultiWindowTextGUI gui;

    public static void main(String... args) throws IOException {
        Screen screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();
        try {
            new Main(screen).showUp();
        } finally {
            screen.stopScreen();
        }
    }

    private Main(Screen screen) {
        gui = new MultiWindowTextGUI(screen);
    }

    private void showUp() {
        File path = new FileDialogBuilder().build().showDialog(gui);
        Optional.ofNullable(path).ifPresent(this::showMainWindow);
    }

    private void showMainWindow(File path) {
        try (Partition partition = new Partition(path.toPath())) {
            Window window = new MainWindow(partition);
            gui.addWindow(window);
            gui.waitForWindowToClose(window);
        } catch (IOException ex) {
            LOG.log(System.Logger.Level.ERROR, "", ex);
            new MessageDialogBuilder()
                    .setTitle(ex.getClass().getName())
                    .setText(ex.getLocalizedMessage())
                    .build().showDialog(gui);
        }
    }
}
