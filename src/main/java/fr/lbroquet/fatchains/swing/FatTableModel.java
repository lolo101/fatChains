package fr.lbroquet.fatchains.swing;

import fr.lbroquet.fatchains.EntryChain;
import fr.lbroquet.fatchains.Partition;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    private final Partition partition;
    private final List<EntryChain> chains;
    private final Map<Integer, String> types = new HashMap<>();
    private final Column[] columns;

    FatTableModel(Partition partition) {
        this.partition = partition;
        this.chains = partition.getEntryChains();
        Column[] c = {
            new Column("Cluster Index", int.class, EntryChain::getHead),
            new Column("Size (KB)", long.class, e -> e.length() * partition.getBootSector().getBytesPerCluster() >> 10),
            new Column("Type", String.class, e -> Optional.ofNullable(types.get(e.getHead())).orElseGet(() -> tryGuessAndCacheType(e)))
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

    private String tryGuessAndCacheType(EntryChain entry) {
        try {
            String type = partition.guessEntryType(entry);
            types.put(entry.getHead(), type);
            return type;
        } catch (IOException ex) {
            return ex.toString();
        }
    }
}
