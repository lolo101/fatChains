package fr.lbroquet.fatchains.lanterna;

import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import fr.lbroquet.fatchains.EntryChain;
import fr.lbroquet.fatchains.Partition;
import java.io.IOException;

class FatPanel extends Panel {

    private final Partition partition;
    private final Table table;
    private final TableModel model;

    public FatPanel(Partition partition) {
        this.partition = partition;
        table = new Table("Index", "Size in clusters", "Size in KB", "Finished", "Type");
        model = table.getTableModel();
    }

    void init() throws IOException {
        partition.getEntryChains().stream().filter(EntryChain::isFinished).forEach(this::addRow);
        addComponent(new Label(String.format("%s chains found", model.getRowCount())));
        addComponent(table);
    }

    private void addRow(EntryChain chain) {
        int head = chain.getHead();
        long length = chain.length();
        long sizeKb = asSizeKb(length);
        boolean finished = chain.isFinished();
        String type = tryGuessFileType(chain);
        model.addRow(head, length, sizeKb, finished, type);
    }

    private long asSizeKb(long length) {
        try {
            return (length * partition.getBootSector().getBytesPerCluster()) >> 10;
        } catch (IOException ex) {
            return 0;
        }
    }

    private String tryGuessFileType(EntryChain chain) {
        try {
            return partition.guessEntryType(chain);
        } catch (IOException ex) {
            return "<error>";
        }
    }
}
