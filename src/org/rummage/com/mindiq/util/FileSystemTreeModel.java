/*
 * (C) 2004 - Geotechnical Software Services This code is free
 * software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version. This code is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General
 * Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Some modifications (C) 2004, Chris Smith
 */
package org.rummage.com.mindiq.util;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.rummage.toolbox.util.ObjectUtils;

/**
 * A TreeModel implementation for a disk directory structure. Typical
 * usage:
 *
 * <pre>
 * FileSystemTreeModel model = new FileSystemTreeModel();
 * FileSystemTreeRenderer renderer = new FileSystemTreeRenderer();
 * JTree tree = new JTree (model);
 * tree.setCellRenderer(renderer);
 * tree.setRootVisible(false);
 * tree.setShowsRootHandles(true);
 * </pre>
 *
 * @author <a href="mailto:jacob.dre...@geosoft.no">Jacob Dreyer </a>
 * @author <a href="mailto:cdsm...@twu.net">Chris Smith </a>
 */
public class FileSystemTreeModel
extends DefaultTreeModel
{
    /*
     * Define a better ordering for sorting files.
     */
    private Comparator<File> sortComparator = new Comparator<File>() {
        public int compare(File a, File b)
        {
            Collator collator = Collator.getInstance();

            if (a.isDirectory() && b.isFile()) return -1;
            else if (a.isFile() && b.isDirectory()) return +1;

            int result = collator.compare(a.getName(), b.getName());
            if (result != 0) return result;

            result = collator.compare(
                    a.getAbsolutePath(), b.getAbsolutePath());
            return result;
        }
    };


    private Collection<TreeModelListener> listeners;

    private Object falseRoot = new Object();

    private File[] roots;
    private FileSystemView fsv;

    private boolean hiddenVisible;

    private HashMap<File, List<File>> sortedChildren;
    private HashMap<File, Long> lastModifiedTimes;

    private boolean useFileHiding = false;

    /**
     * Create a tree model using the specified file system view and
     * the specified roots.  This results in displaying a subset of
     * the actual filesystem.  There need not be any specific
     * relationship between the roots specified.
     *
     * @param fsv   The FileSystemView implementation
     * @param roots Root files
     */
    public FileSystemTreeModel(FileSystemView fsv, File[] roots)
    {
        super(new WindowsDesktopTreeNode(fsv));
        this.fsv = fsv;
        this.roots = roots;

        listeners = new ArrayList<TreeModelListener>();
        sortedChildren = new HashMap<File, List<File>>();
        lastModifiedTimes = new HashMap<File, Long>();
    }

    /**
     * Create a tree model using the specified file system view.
     *
     * @param fsv The FileSystemView implementation
     */
    public FileSystemTreeModel(FileSystemView fsv)
    {
        this(fsv, fsv.getRoots());
    }

    /**
     * Create a tree model using the default file system view for this
     * platform.
     */
    public FileSystemTreeModel()
    {
        this(FileSystemView.getFileSystemView());
    }

    public FileSystemView getFileSystemView() {
        return fsv;
    }

    public Object getRoot()
    {
        return falseRoot;
    }

    public Object getChild(Object parent, int index)
    {
        if (parent == falseRoot)
        {
            return roots[index];
        }
        else
        {
            List children = (List) sortedChildren.get(parent);
            return
            children == null ? null : index < children.size() ?
                    children.get(index) : null;
        }
    }

    public int getChildCount(Object parent)
    {
        if (parent == falseRoot)
        {
            return roots.length;
        }
        
        File file = (File) parent;
        parent = maybeResolveLink((File) parent);
        if (!fsv.isTraversable(file)) return 0;

        List<File> children =
            Arrays.asList(fsv.getFiles(file, !hiddenVisible));

        List<File> directories = new ArrayList<File>();

        if (useFileHiding) {
            for (File child : children) {
                child = maybeResolveLink(child);
                if (child != null && child.isDirectory())
                    directories.add(child);
            }
        }

        int nChildren = 0;
        
        if (useFileHiding) {
            nChildren = directories.size();
        }
        else if (children != null){
            nChildren = children.size();
        }

        long lastModified = file.lastModified();

        boolean isFirstTime = lastModifiedTimes.get(file) == null;
        boolean isChanged = false;

        if (!isFirstTime)
        {
            Long modified = (Long) lastModifiedTimes.get(file);
            long diff = Math.abs(modified.longValue()
                    - lastModified);

            // MS/Win or Samba HACK. Check this!
            isChanged = diff > 4000;
        }

        // Sort and register children info
        if (isFirstTime || isChanged)
        {
            lastModifiedTimes.put(file, new Long(lastModified));

            TreeSet<File> sorted = new TreeSet<File>(sortComparator);

            for (File child : children)
            {
                sorted.add(child);
            }

            sortedChildren.put(file, new ArrayList<File>(sorted));
        }

        // Notify listeners (visual tree typically) if changes
        if (isChanged)
        {
            TreeModelEvent event =
                new TreeModelEvent((TreeModel) this, getTreePath(file));

            fireTreeStructureChanged(event);
        }

        return nChildren;
    }

    private Object[] getTreePath(Object obj)
    {
        List<Object> path = new ArrayList<Object>();
        while (obj != falseRoot)
        {
            path.add(obj);
            obj = fsv.getParentDirectory((File) obj);
        }

        path.add(falseRoot);

        int nElements = path.size();
        Object[] treePath = new Object[nElements];

        for (int i = 0; i < nElements; i++)
        {
            treePath[i] = path.get(nElements - i - 1);
        }

        return treePath;
    }

    public boolean isLeaf(Object node)
    {
        if (node == falseRoot) {
            return false;
        }
        else {
            FileTreeNode fileTreeNode = new FileTreeNode((File) node, this);
            
            File file = (File) fileTreeNode.getFile();
            
            return !fsv.isTraversable(maybeResolveLink(file));
        }
    }

    public void valueForPathChanged(TreePath path, Object newValue)
    {
    }

    public int getIndexOfChild(Object parent, Object child)
    {
        List children = (List) sortedChildren.get(parent);
        return children.indexOf(child);
    }

    public void addTreeModelListener(TreeModelListener listener)
    {
        if (listener != null && !listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeTreeModelListener(TreeModelListener listener)
    {
        if (listener != null) listeners.remove(listener);
    }

    public void fireTreeNodesChanged(TreeModelEvent event)
    {
        for (Iterator i = listeners.iterator(); i.hasNext();)
        {
            TreeModelListener listener = (TreeModelListener) i.next();
            listener.treeNodesChanged(event);
        }
    }

    public void fireTreeNodesInserted(TreeModelEvent event)
    {
        for (Iterator i = listeners.iterator(); i.hasNext();)
        {
            TreeModelListener listener = (TreeModelListener) i.next();
            listener.treeNodesInserted(event);
        }
    }

    public void fireTreeNodesRemoved(TreeModelEvent event)
    {
        for (Iterator i = listeners.iterator(); i.hasNext();)
        {
            TreeModelListener listener = (TreeModelListener) i.next();
            listener.treeNodesRemoved(event);
        }
    }

    public void fireTreeStructureChanged(TreeModelEvent event)
    {
        for (Iterator i = listeners.iterator(); i.hasNext();)
        {
            TreeModelListener listener = (TreeModelListener) i.next();
            listener.treeStructureChanged(event);
        }
    }

    public File maybeResolveLink(File file) {
        File linkedTo = null;
        
        if (file != null && file.getPath().endsWith(".lnk")) {
            try {
                //sun.awt.shell.ShellFolder shellFolder =
                //    sun.awt.shell.ShellFolder.getShellFolder(file);
                Object shellFolderObject =
                    ObjectUtils.maybeInvokeMethod("sun.awt.shell.ShellFolder",
                    "getShellFolder", file);

                //linkedTo = (File) shellFolder.getLinkLocation();
                linkedTo = 
                    (File) ObjectUtils.maybeInvokeMethod(shellFolderObject,
                    "getLinkLocation");
            }
            catch (Exception ex) {
                String s = ex.getMessage();
                
                if (s != null) {
                
                // Get link path but strip off "Could Not Find File " the
                // first 20 characters
                String linkPath =
                    ex.getMessage().substring(20, ex.getMessage().length());
                
                linkedTo = new File(linkPath);
                
                }
            }
        }

        return linkedTo == null ? file : linkedTo;
    }


    /**
     * Returns true if hidden files are not shown in the file chooser;
     * otherwise, returns false.
     *
     * @return the status of the file hiding property
     * @see #setFileHidingEnabled
     */
    public boolean isFileHidingEnabled() {
        return useFileHiding;
    }

    /**
     * Sets file hiding on or off. If true, hidden files are not shown
     * in the file chooser. The job of determining which files are
     * shown is done by the <code>FileView</code>.
     *
     * @beaninfo
     *   preferred: true
     *       bound: true
     * description: Sets file hiding on or off.
     *
     * @param b the boolean value that determines whether file hiding is
     *          turned on
     * @see #isFileHidingEnabled
     */
    public void setFileHidingEnabled(boolean b) {
        // Dump showFilesListener since we'll ignore it from now on
        //if (showFilesListener != null) {
        //    Toolkit.getDefaultToolkit().removePropertyChangeListener(SHOW_HIDDEN_PROP, showFilesListener);
        //    showFilesListener = null;
        //} 
        boolean oldValue = useFileHiding;
        useFileHiding = b;
        //firePropertyChange(FILE_HIDING_CHANGED_PROPERTY, oldValue, useFileHiding);
    }

    public void setLeavesVisible(boolean b) {
        // TODO Auto-generated method stub
        
    }
}
