package fr.lbroquet.fatchains.swing;

import fr.lbroquet.fatchains.EntryChain;
import fr.lbroquet.fatchains.Partition;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;

public class FatTableModel extends AbstractTableModel {

    private final Partition partition;
    private final List<EntryChain> chains;

    FatTableModel(Partition partition) throws IOException {
        this.chains = partition.getFat().getHeads().chains().collect(Collectors.toList());
        this.partition = partition;
    }

    @Override
    public int getRowCount() {
        return chains.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case 0: return "Cluster Index";
            case 1: return "Size (KB)";
            case 2: return "Type";
            default: return "?";
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            switch(columnIndex) {
                case 0: return chains.get(rowIndex).getHead();
                case 1: return chains.get(rowIndex).length() * partition.getBootSector().getBytesPerCluster() >> 10;
                case 2: return "?";
                default: return "?";
            }
        } catch (IOException ex) {
            return "<error>";
        }
    }
}
