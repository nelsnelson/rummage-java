/*
 * FileTreeNode.java
 *
 * Created on September 23, 2006, 10:56 PM
 */

package org.rummage.com.mindiq.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeModel;

import org.rummage.slide.tree.LazyMutableTreeNode;

/**
 *
 * @author nnelson
 */


public class FileTreeNode extends LazyMutableTreeNode implements Comparable {
    private FileSystemTreeModel fileSystemTreeModel;
    
    public FileTreeNode(File file) {
        super(file);
    }
    
    public FileTreeNode(File file, FileSystemTreeModel fileSystemTreeModel) {
        super(file);
        
        this.fileSystemTreeModel = fileSystemTreeModel;
    }
    
    public boolean canEnqueue() {
        return !isLoaded()
        && !FileSystemView.getFileSystemView().isFloppyDrive(getFile())
        && !FileSystemView.getFileSystemView().isFileSystemRoot(getFile());
    }
    
    public boolean isLeaf() {
        if (!isLoaded()) {
            return false;
        } else {
            return super.isLeaf();
        }
    }
    
    protected void loadChildren() {
        FileTreeNode[] nodes = getChildren();
        for (FileTreeNode node : nodes) {
            add(node);
            
            de.bug.ging(node.getClass());
        }
    }
    
    private FileTreeNode[] getChildren() {
        File[] files =
            fileSystemTreeModel.getFileSystemView().getFiles(
            getFile(),
            fileSystemTreeModel.isFileHidingEnabled());
        ArrayList nodes = new ArrayList();
        // keep only directories, no "file" in the tree.
        if (files != null) {
            for (int i = 0, c = files.length; i < c; i++) {
                if (files[i].isDirectory()) {
                    nodes.add(new FileTreeNode(files[i]));
                }
            }
        }
        // sort directories, FileTreeNode implements Comparable
        FileTreeNode[] result = (FileTreeNode[])nodes
            .toArray(new FileTreeNode[0]);
        Arrays.sort(result);
        return result;
    }
    
    public File getFile() {
        return (File)getUserObject();
    }
    
    public String toString() {
        return FileSystemView.getFileSystemView().getSystemDisplayName(
            (File)getUserObject());
    }
    
    public int compareTo(Object o) {
        if (!(o instanceof FileTreeNode)) { return 1; }
        return getFile().compareTo(((FileTreeNode)o).getFile());
    }
    
    public void clear() {
        super.clear();
        ((DefaultTreeModel)fileSystemTreeModel).nodeStructureChanged(this);
    }
}
