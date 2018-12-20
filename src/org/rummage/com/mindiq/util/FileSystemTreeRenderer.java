/*
 * FileSystemTreeCellRenderer.java
 *
 * Created on May 10, 2006, 10:57 AM
 */

package org.rummage.com.mindiq.util;

import java.awt.Component;
import java.io.File;

import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.rummage.toolbox.util.Properties;
import org.rummage.toolbox.util.UIManager;

public class FileSystemTreeRenderer
    extends DefaultTreeCellRenderer
{
    private FileSystemView fsv;
 
    public FileSystemTreeRenderer(FileSystemView fsv)
    {
        super();
        
        this.fsv = fsv;
    }
    
    public FileSystemTreeRenderer()
    {
        this(FileSystemView.getFileSystemView());
    }
    
    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean sel, boolean expanded, boolean leaf, int row, 
        boolean hasFocus) 
    {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, false,
            row, hasFocus);
        
        if (value instanceof FileTreeNode ||
            value instanceof WindowsDesktopTreeNode)
        {
            FileTreeNode node = (FileTreeNode) value;
            
            File file = node.getFile();
            
            setText(fsv.getSystemDisplayName(file));
            
            if (Properties.isMacOSX() && UIManager.getLookAndFeel().isNativeLookAndFeel()) {
                // ?
            }
            else {
                setIcon(fsv.getSystemIcon(node.getFile()));
            }
        }
        else {
            try {
                if (value.getClass().equals(Class.
                    forName("sun.awt.shell.Win32ShellFolder2"))) 
                {
                    FileTreeNode node = new FileTreeNode((File) value);
                    
                    getTreeCellRendererComponent(tree, node, sel, expanded, 
                        leaf, row, hasFocus);
                }
            }
            catch (ClassNotFoundException ex) {
                
            }
        }
        
        return this;
    }
}
