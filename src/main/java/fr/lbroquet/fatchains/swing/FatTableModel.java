package fr.lbroquet.fatchains.swing;

import fr.lbroquet.fatchains.EntryChain;
import fr.lbroquet.fatchains.Partition;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;

public class FatTableModel extends AbstractTableModel {

    private final List<EntryChain> chains;

    FatTableModel(Partition partition) throws IOException {
        this.chains = partition.getFat().getHeads().chains().collect(Collectors.toList());
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
        return chains.get(rowIndex).getHead();
    }
}
