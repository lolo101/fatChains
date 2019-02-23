package fr.lbroquet.fatchains.swing;

import fr.lbroquet.fat.File;
import fr.lbroquet.fat.FileDirectory;
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
                .map(FileDirectory::new)
                .map(this::asTreeNode)
                .forEach(mutableRoot::add);
    }

    private boolean isFileDirectory(EntryChain c) {
        return c.getType().startsWith("FAT FileDirectory");
    }

    private MutableTreeNode asTreeNode(FileDirectory directory) {
        DefaultMutableTreeNode dir = new DefaultMutableTreeNode(directory);
        for (File file : directory) {
            dir.add(new DefaultMutableTreeNode(file, false));
        }
        return dir;
    }
}
