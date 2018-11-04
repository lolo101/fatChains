package fr.lbroquet.fatchains.swing;

import fr.lbroquet.fatchains.EntryChain;
import fr.lbroquet.fatchains.Partition;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

public class DirectoryTreeModel extends DefaultTreeModel {

    public DirectoryTreeModel(Partition partition) {
        super(new DefaultMutableTreeNode());
        DefaultMutableTreeNode mutableRoot = (DefaultMutableTreeNode) getRoot();
        partition.getEntryChains().stream()
                .filter(this::isFileDirectory)
                .map(this::asTreeNode)
                .forEach(mutableRoot::add);
    }

    private boolean isFileDirectory(EntryChain c) {
        return c.getType().startsWith("FAT FileDirectory");
    }

    private MutableTreeNode asTreeNode(EntryChain chain) {
        return new DefaultMutableTreeNode(chain);
    }
}
