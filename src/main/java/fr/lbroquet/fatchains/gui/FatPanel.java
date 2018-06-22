package fr.lbroquet.fatchains.gui;

import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import fr.lbroquet.fatchains.EntryChain;
import java.util.stream.Stream;

class FatPanel extends Panel {

    private final Table table;
    private final TableModel model;

    public FatPanel() {
        table = new Table("Index", "Size");
        model = table.getTableModel();
    }

    void init(Stream<EntryChain> chains) {
        chains.forEach(this::addRow);
        addComponent(new Label(String.valueOf(model.getRowCount())));
        addComponent(table);
    }

    private void addRow(EntryChain chain) {
        int head = chain.getHead();
        long length = chain.length();
        model.addRow(head, length);
    }
}
