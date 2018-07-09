package fr.lbroquet.fatchains.swing;

import java.io.IOException;
import java.nio.file.Path;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String... args) throws IOException {
        final JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(null);
        if (option == JFileChooser.OPEN_DIALOG) {
            showFat(fileChooser.getSelectedFile().toPath());
        }
    }

    private static void showFat(Path path) throws IOException {
        JFrame frame = new MainFrame(path);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
}
