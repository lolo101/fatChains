package fr.lbroquet.fatchains.gui;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.dialogs.FileDialogBuilder;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import fr.lbroquet.fatchains.Partition;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Main {

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
        Partition partition = new Partition(path.toPath());
        Window window = new MainWindow(partition);
        gui.addWindow(window);
        gui.waitForWindowToClose(window);
    }
}
