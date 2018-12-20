/*
 * WindowsDesktopTreeNode.java
 *
 * Created on September 23, 2006, 10:54 PM
 */

package org.rummage.com.mindiq.util;

import java.io.File;
import java.util.Arrays;

import javax.swing.filechooser.FileSystemView;

import org.rummage.slide.tree.LazyMutableTreeNode;

/**
 *
 * @author nnelson
 */
public class WindowsDesktopTreeNode
    extends LazyMutableTreeNode
{
    private FileSystemTreeModel fileSystemTreeModel;
    
    public WindowsDesktopTreeNode(FileSystemView fsv) {
        super(fsv);
    }
    
    public WindowsDesktopTreeNode(FileSystemView fsv, FileSystemTreeModel fileSystemTreeModel) {
        super(fsv);
        
        this.fileSystemTreeModel = fileSystemTreeModel;
    }

    protected void loadChildren() {
        FileSystemView fsv = (FileSystemView)getUserObject();
        File[] roots = fsv.getRoots();
        if (roots != null) {
            Arrays.sort(roots);
            for (int i = 0, c = roots.length; i < c; i++) {
                add(new FileTreeNode(roots[i], fileSystemTreeModel));
            }
        }
    }

    public String toString() {
      return "/";
    }
}
