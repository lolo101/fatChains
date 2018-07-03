package fr.lbroquet.fatchains.swing;

import fr.lbroquet.fatchains.Partition;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String... args) {
        SwingUtilities.invokeLater(Main::doRun);
    }

    private static void doRun() {
        final JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(null);
        if (option == JFileChooser.OPEN_DIALOG) {
            showFat(fileChooser.getSelectedFile().toPath());
        }
    }

    private static void showFat(Path path) {
        try (Partition partition = new Partition(path)) {
            JFrame frame = new MainFrame(new FatTableModel(partition));
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
