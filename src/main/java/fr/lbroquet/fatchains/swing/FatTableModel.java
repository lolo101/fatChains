package fr.lbroquet.fatchains.swing;

import fr.lbroquet.fatchains.EntryChain;
import fr.lbroquet.fatchains.Partition;
import java.util.List;
import java.util.function.Function;
import javax.swing.table.AbstractTableModel;

public class FatTableModel extends AbstractTableModel {

    private static class Column {
        private final String title;
        private final Class<?> type;
        private final Function<EntryChain, ?> value;

        <T> Column(String title, Class<T> type, Function<EntryChain, T> value) {
            this.title = title;
            this.type = type;
            this.value = value;
        }
    }

    private final List<EntryChain> chains;
    private final Column[] columns;

    FatTableModel(Partition partition) {
        this.chains = partition.getEntryChains();
        Column[] c = {
            new Column("Entry Index", int.class, EntryChain::getHeadEntryIndex),
            new Column("Size (KB)", long.class, e -> e.length() * partition.getBootSector().getBytesPerCluster() >> 10),
            new Column("Type", String.class, EntryChain::getType)
        };
        this.columns = c;
    }

    @Override
    public int getRowCount() {
        return chains.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex].title;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns[columnIndex].type;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return columns[columnIndex].value.apply(chains.get(rowIndex));
    }
}
