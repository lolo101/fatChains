package fr.lbroquet.fatchains.gui;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import fr.lbroquet.fatchains.EntryChain;
import java.util.Collection;

class FatPanel extends Panel {

    private final Table table;
    private final TableModel model;

    public FatPanel() {
        table = new Table("Index", "Size");
        model = table.getTableModel();
    }

    void init(Collection<EntryChain> chains) {
        chains.stream().forEach(this::addRow);
        addComponent(table);
    }

    private void addRow(EntryChain chain) {
        int head = chain.getHead();
        long length = chain.length();
        model.addRow(head, length);
    }
}
