/*
 * DirectoryChooser.java
 *
 * Created on October 1, 2006, 4:59 PM
 */

package org.rummage.slide;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.rummage.toolbox.util.ClassUtils;
import org.rummage.toolbox.util.ObjectUtils;

public class JFileSystemTree
    extends JTree
    implements TreeSelectionListener, MouseListener
{
    protected static FileSystemView fsv = FileSystemView.getFileSystemView();
    private SwingWorker nodeExpansionThread = null;
    private TreeSelectionModel treeSelectionModel = null;
    private javax.swing.Timer disableTreeSelcetionTimer =null;
    private boolean nodeExpansionInProcess = false;
    
    /*--- Begin Public API -----*/
    
    /* --constructors*/
    public JFileSystemTree() {
        this(null);
    }
    
    public JFileSystemTree(File directory) {
        super(new FileSystemNode(fsv.getRoots()[0]));
        
        setCellRenderer(new FileSystemTreeRenderer());
        addTreeWillExpandListener(new MyTreeWillExpandListener());
        getSelectionModel().setSelectionMode(TreeSelectionModel.
            SINGLE_TREE_SELECTION);
        
        setSelectedDirectory(directory);
        addTreeSelectionListener(this);
        addMouseListener(this);
    }
    
    private void enableTreeSelection() {
        if (treeSelectionModel != null) {
            setSelectionModel(treeSelectionModel);
            treeSelectionModel = null;
        }
    }
    
    private void disableTreeSelection() {
        disableTreeSelcetionTimer =
            new javax.swing.Timer(100, new ActionListener()
        {
            public void actionPerformed(ActionEvent evt) {
                if (nodeExpansionInProcess && treeSelectionModel == null) {
                    treeSelectionModel =
                        JFileSystemTree.this.getSelectionModel();
                    
                    JFileSystemTree.this.setSelectionModel(null);
                }
                else {
                    JFileSystemTree.this.stopTimer();
                }
            }});
            disableTreeSelcetionTimer.start();
    }
    
    private void stopTimer() {
        if (disableTreeSelcetionTimer != null) {
            disableTreeSelcetionTimer.stop();
        }
    }
    
    private class MyTreeWillExpandListener
        implements TreeWillExpandListener
    {
        public void treeWillExpand(TreeExpansionEvent evt)
            throws ExpandVetoException
        {
            if (nodeExpansionInProcess) {
                throw new ExpandVetoException(evt);
            }
            
            JFileSystemTree tree = JFileSystemTree.this;
            tree.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            
            TreePath path = evt.getPath();
            FileSystemNode node = (FileSystemNode)
            path.getLastPathComponent();
            boolean areChildrenLoaded = node.areChildrenLoaded();
            
            if (!areChildrenLoaded) {
                nodeExpansionThread = new NodeExpansionThread(path);
                nodeExpansionThread.start();
                nodeExpansionInProcess = true;
                tree.disableTreeSelection();
                throw new ExpandVetoException(evt);
            }
            
            tree.enableTreeSelection();
            tree.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        
        public void treeWillCollapse(TreeExpansionEvent evt)
            throws ExpandVetoException
        {
            if (nodeExpansionInProcess) {
                throw new ExpandVetoException(evt);
            }
        }
    }
    
    private class NodeExpansionThread
        extends SwingWorker
    {
        TreePath treePath = null;
        
        public NodeExpansionThread(TreePath path) {
            this.treePath = path;
        }
        
        public Object construct() {
            FileSystemNode node = (FileSystemNode)
            treePath.getLastPathComponent();
            node.children();
            nodeExpansionInProcess = false;
            
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JFileSystemTree.this.expandPath(treePath);
                }
            });
            
            return node;
        }
    }
    
    public void setSelectedDirectory(File directory) {
        if (directory == null) {
            directory = fsv.getDefaultDirectory();
        }
        
        setSelectionPath(mkPath(directory));
    }
    
    public File getSelectedDirectory() {
        FileSystemNode node = (FileSystemNode) getLastSelectedPathComponent();
        
        if (node != null) {
            File directory = node.getDirectory();
            
            //experimental!
            directory = maybeResolveLink(directory);
            
            if (fsv.isFileSystem(directory)) {
                return directory;
            }
        }
        return null;
    }
    
    public File maybeResolveLink(File file) {
        File linkedTo = null;
        
        if (file != null && file.getPath().endsWith(".lnk")) {
            try {
            	Object shellFolderObject =
            		ObjectUtils.maybeInvokeMethod("sun.awt.shell.ShellFolder",
            	    "getShellFolder", file);
            	
                linkedTo = 
                	(File) ObjectUtils.maybeInvokeMethod(shellFolderObject,
                    "getLinkLocation");
            }
            catch (Exception ex) {
                // Get link path but strip off "Could Not Find File " the
                // first 20 characters
                String linkPath =
                    ex.getMessage().substring(20, ex.getMessage().length());

                linkedTo = new File(linkPath);
            }
	    }
            
        return linkedTo == null ? file : linkedTo;
    }
    
    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }
    
    public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }
    
    public ActionListener[] getActionListeners() {
        return (ActionListener[])listenerList.getListeners(ActionListener.class);
    }
    
    /*--- End Public API -----*/
    
    
    
    
    /*--- TreeSelectionListener Interface -----*/
    
    public void valueChanged(TreeSelectionEvent ev) {
        File oldDir = null;
        TreePath oldPath = ev.getOldLeadSelectionPath();
        if (oldPath != null) {
            oldDir = ((FileSystemNode)oldPath.getLastPathComponent()).getDirectory();
            if (!fsv.isFileSystem(oldDir)) {
                oldDir = null;
            }
        }
        File newDir = getSelectedDirectory();
        firePropertyChange("selectedDirectory", oldDir, newDir);
    }
    
    /*--- MouseListener Interface -----*/
    
    public void mousePressed(MouseEvent e) {
        if (e.getClickCount() == 2) {
            TreePath path = getPathForLocation(e.getX(), e.getY());
            if (path != null && path.equals(getSelectionPath()) &&
                getSelectedDirectory() != null) {
                
                fireActionPerformed("dirSelected", e);
            }
        }
    }
    
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    
    
    /*--- Private Section ------*/
    
    private TreePath mkPath(File directory) {
        FileSystemNode root = (FileSystemNode)getModel().getRoot();
        if (root.getDirectory().equals(directory)) {
            return new TreePath(root);
        }
        
        TreePath parentPath = mkPath(fsv.getParentDirectory(directory));
        FileSystemNode parentNode = (FileSystemNode)parentPath.getLastPathComponent();
        Enumeration enumeration = parentNode.children();
        while (enumeration.hasMoreElements()) {
            FileSystemNode child = (FileSystemNode)enumeration.nextElement();
            if (child.getDirectory().equals(directory)) {
                return parentPath.pathByAddingChild(child);
            }
        }
        return null;
    }
    
    
    private void fireActionPerformed(String command, InputEvent evt) {
        ActionEvent e =
            new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command,
            evt.getWhen(), evt.getModifiers());
        
        ActionListener[] listeners = getActionListeners();
        
        for (ActionListener listener : listeners) {
            listener.actionPerformed(e);
        }
    }
    
    private static class FileSystemNode
        extends DefaultMutableTreeNode
    {
        private boolean chlidrenLoaded = false;
        private int childrenCount = 0;
        
        public FileSystemNode(File f) {
            super(f);
        }
        
        public File getDirectory() {
            return (File) userObject;
        }
        
        public boolean areChildrenLoaded() {
            return  chlidrenLoaded;
        }
        
        public int getChildCount() {
            if (!chlidrenLoaded) {
                populateChildren();
                childrenCount = super.getChildCount();
                chlidrenLoaded = true;
            }
            
            return childrenCount;
        }
        
        public Enumeration children() {
            if (!chlidrenLoaded) {
                populateChildren();
                childrenCount = super.getChildCount();
                chlidrenLoaded = true;
            }
            
            return super.children();
        }
        
        public boolean isLeaf() {
            return false;
        }
        
        private void populateChildren() {
            if (children == null) {
                File[] files = fsv.getFiles(getDirectory(), true);
                Arrays.sort(files);
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    
                    if (fsv.isTraversable(f).booleanValue()) {
                        insert(new FileSystemNode(f),
                            (children == null) ? 0 : children.size());
                    }
                }
            }
        }
        
        public String toString() {
            return JFileSystemTree.fsv.getSystemDisplayName(getDirectory());
        }
        
        public boolean equals(Object o) {
            return
                o instanceof FileSystemNode &&
                userObject.equals(((FileSystemNode) o).userObject);
        }

        public File getFile() {
            Object o = getUserObject();
            
            return o instanceof File ? (File) o : null;
        }
    }
    
    private static class FileSystemTreeRenderer
        extends DefaultTreeCellRenderer
    {
        public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel, boolean expanded, boolean leaf, int row,
            boolean hasFocus)
        {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, false,
                row, hasFocus);
            
            // even "leaf" folders should look like other folders
            if (value instanceof FileSystemNode) {
                FileSystemNode node = (FileSystemNode)value;
                setText(JFileSystemTree.fsv.getSystemDisplayName(node.getFile()));
                
                if (org.rummage.toolbox.util.Properties.isMacOSX() &&
                    UIManager.getLookAndFeel().isNativeLookAndFeel())
                {
                    // do not set icon for MacOSX when native look is used, it
                    // seems the Tree.icons set by the look and feel are not
                    // that good or Apple is doing something weird.
                    // Use <code>setIcon()</code> only if not running in
                    // MacOS X.
                }
                else {
                    setIcon(JFileSystemTree.fsv.getSystemIcon(node.getFile()));
                }
            }
            
            return this;
        }
    }
}
