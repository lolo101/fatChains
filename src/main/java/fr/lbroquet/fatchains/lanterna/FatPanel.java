package fr.lbroquet.fatchains.lanterna;

import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import fr.lbroquet.fatchains.EntryChain;
import fr.lbroquet.fatchains.Partition;

class FatPanel extends Panel {

    private final Partition partition;
    private final Table table;
    private final TableModel model;

    public FatPanel(Partition partition) {
        this.partition = partition;
        table = new Table("Entry Index", "Size in clusters", "Size in KB", "Finished", "Type");
        model = table.getTableModel();
    }

    void init() {
        partition.getEntryChains().stream().filter(EntryChain::isFinished).forEach(this::addRow);
        addComponent(new Label(String.format("%s chains found", model.getRowCount())));
        addComponent(table);
    }

    private void addRow(EntryChain chain) {
        int entryIndex = chain.getHeadEntryIndex();
        long length = chain.length();
        long sizeKb = asSizeKb(length);
        boolean finished = chain.isFinished();
        String type = tryGuessFileType(chain);
        model.addRow(entryIndex, length, sizeKb, finished, type);
    }

    private long asSizeKb(long length) {
        return (length * partition.getBootSector().getBytesPerCluster()) >> 10;
    }

    private String tryGuessFileType(EntryChain chain) {
        return chain.getType();
    }
}
