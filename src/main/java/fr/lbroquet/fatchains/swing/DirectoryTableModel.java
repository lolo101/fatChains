package fr.lbroquet.fatchains.swing;

import fr.lbroquet.fatchains.EntryChain;
import fr.lbroquet.fatchains.Partition;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;

public class DirectoryTableModel extends AbstractTableModel {

    private final List<EntryChain> chains;

    public DirectoryTableModel(Partition partition) throws IOException {
        chains = partition.getEntryChains().stream().filter(this::isFileDirectory).collect(Collectors.toList());
    }

    private boolean isFileDirectory(EntryChain c) {
        return c.getType().startsWith("FAT FileDirectory");
    }

    @Override
    public int getRowCount() {
        return chains.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return chains.get(rowIndex).getClusterIndex();
    }
}
