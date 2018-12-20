/*
 * JTree.java
 *
 * Created on May 9, 2006, 4:56 PM
 */

package org.rummage.slide;

import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.Position;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author nelsnelson
 */
public class JTree
    extends javax.swing.JTree
    implements MouseListener
{
    public JTree() {
        super();
        addMouseListener(this);
    }
    
    public JTree(TreeNode treeNode) {
        super(treeNode);
        addMouseListener(this);
    }
    
    public JTree(TreeModel treeModel) {
        super(treeModel);
        addMouseListener(this);
    }
    
    public TreeNode getRoot() {
        return (TreeNode) getModel().getRoot();
    }
    
    public int getScrollableBlockIncrement(Rectangle visibleRect,
        int orientation, int direction)
    {
        super.getScrollableBlockIncrement(visibleRect, orientation, direction);
        
        int n = getRowCount();
        
        int h = getRowHeight();
        
        int H = visibleRect.height;
        
        int max = H - (H % h);
        
        return Math.min(max, h * 3);
    }
    
    public int getScrollableUnitIncrement(Rectangle visibleRect,
        int orientation, int direction)
    {
        super.getScrollableBlockIncrement(visibleRect, orientation, direction);
        
        int n = getRowCount();
        
        int h = getRowHeight();
        
        int H = visibleRect.height;
        
        int max = H - (H % h);
        
        return Math.min(max, 50);
    }
    
    public TreePath find(String text) {
        return find(text, 0, Position.Bias.Forward);
    }
    
    public TreePath find(String text, int startRow) {
        return find(text, startRow, Position.Bias.Forward);
    }
    
    public TreePath find(String text, int startRow, Position.Bias bias) {
        return getNextMatch(text, startRow, bias);
    }
    
    // Finds the path in tree as specified by the array of names. The names array is a
    // sequence of names where names[0] is the root and names[i] is a child of names[i-1].
    // Comparison is done using String.equals(). Returns null if not found.
    public TreePath findByName(String[] names) {
        TreeNode root = (TreeNode)getModel().getRoot();
        return find2(new TreePath(root), names, 0, true);
    }
    
    private TreePath find2(TreePath parent, Object[] nodes, int depth, boolean byName) {
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        Object o = node;
    
        // If by name, convert node to a string
        if (byName) {
            o = o.toString();
        }
    
        // If equal, go down the branch
        if (o.equals(nodes[depth])) {
            // If at end, return match
            if (depth == nodes.length-1) {
                return parent;
            }
    
            // Traverse children
            if (node.getChildCount() >= 0) {
                for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                    TreeNode n = (TreeNode)e.nextElement();
                    TreePath path = parent.pathByAddingChild(n);
                    TreePath result = find2(path, nodes, depth+1, byName);
                    // Found a match
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
    
        // No match at this branch
        return null;
    }

    // Returns a TreePath containing the specified node.
    public TreePath getPath(TreeNode node) {
        List list = new ArrayList();
        
        // Add all nodes to list
        while (node != null) {
            list.add(node);
            node = node.getParent();
        }
        Collections.reverse(list);
        
        // Convert array of nodes to TreePath
        return new TreePath(list.toArray());
    }
    
    public void expandAllRows() {
        for (int i = 0; i < getRowCount(); i++) {
            expandRow(i);
        }
    }
    
    public void expandAll() {
        expandAll(-1);
    }
    
    //transient private Hashtable expandedState;
    
    public void expandAll(int n) {
        for (int i = (n < 0 ? 0 : getRowCount());
            n < 0 ? n < getRowCount() : n >= n;
            i = i + (n < 0 ? 1 : -1))
        {
            expandRow(i);
        }
    }
    
    /**
     * Expands all nodes in a JTree.
     *
     * @param tree      The JTree to expand.
     * @param depth     The depth to which the tree should be expanded.  Zero
     *                  will just expand the root node, a negative value will
     *                  fully expand the tree, and a positive value will
     *                  recursively expand the tree to that depth.
     */
    public void expandAllNodes(int depth) {
        expandJTreeNode(getModel().getRoot(), 0, depth);
    }
    
    /**
     * Expands a given node in a JTree.
     *
     * @param tree      The JTree to expand.
     * @param model     The TreeModel for tree.     
     * @param node      The node within tree to expand.     
     * @param row       The displayed row in tree that represents
     *                  node.     
     * @param depth     The depth to which the tree should be expanded. 
     *                  Zero will just expand node, a negative
     *                  value will fully expand the tree, and a positive
     *                  value will recursively expand the tree to that
     *                  depth relative to node.
     */
    public int expandJTreeNode(Object node, int row, int depth) {
        TreeModel model = getModel();
        
        if (node != null  &&  !model.isLeaf(node)) {
            expandRow(row);
            if (depth != 0) {
                for (int index = 0;
                     row + 1 < getRowCount()  &&  
                     index < model.getChildCount(node);
                     index++)
                {
                    row++;
                    Object child = model.getChild(node, index);
                    
                    if (child == null) {
                        break;
                    }
                    
                    javax.swing.tree.TreePath path;
                    while ((path = getPathForRow(row)) != null  &&
                           path.getLastPathComponent() != child)
                    {
                        row++;
                    }
                    
                    if (path == null) {
                        break;
                    }
                    
                    row = expandJTreeNode(child, row, depth - 1);
                }
            }
        }
        
        return row;
    }
    
    public void collapseAllRows() {
        for (int i = getRowCount(); i >= 0; i--) {
            if (i > getRowCount() - 1) {
                continue;
            }
            
            expandRow(i);
        }
    }
    
    public void collapseAll() {
        collapseAll(0);
    }
    
    public void collapseAll(int n) {
        for (int i = getRowCount(); i >= n; i--) {
            collapseRow(i);
        }
    }
    
    public class RowDragAdapter
        implements DragSourceListener
    {
        public void dragGestureRecognized(DragGestureEvent event) {
            /* get the selected elements in the tree */
            TreePath selected[] = getSelectionPaths();
            
            // don't start the drag if nothing was selected
            if (selected == null || selected.length == 0)
                return;
            
            TreeNode nodes[] = new TreeNode[selected.length];
            for (int i = 0; i < selected.length; i++) {
                nodes[i] = (TreeNode)selected[i].getLastPathComponent();
            }
            
            // custom transferable object
            //MultiTreeNodeTransferable transferable = new
            //    MultiTreeNodeTransferable(this, nodes);
            
            // this method is in my subclass of JTree
            TreeCellRenderer renderer = JTree.this.getCellRenderer();
            JPanel imagePanel;
            
            // I didn't want to display more than 10 rows being dragged. In your
            // case I don't think you need to worry about this.
            if (nodes.length <= 10) {
                imagePanel = new JPanel(new GridLayout(nodes.length, 1));
                for (int i = 0; i < nodes.length; i++) {
                    imagePanel.add(renderer.getTreeCellRendererComponent(JTree.this,
                        nodes[i], false, false, false, 0, false));
                }
            }
            else {
                imagePanel = new JPanel(new GridLayout(11,1));
                for (int i = 0; i < 10; i++) {
                    imagePanel.add(renderer.getTreeCellRendererComponent(JTree.this,
                        nodes[i], false, false, false, 0, false));
                }
                
                // this is only to show that there are more rows when more than 10 are selected
                imagePanel.add(new JLabel("..."));
            }
            
            // I create this JFrame so that the created panel sets its size correctly.
            // I couldn't find any other way to get it to work, just ended up with a 0x0 image.
            JFrame temp = new JFrame();
            temp.setContentPane(imagePanel);
            temp.pack();
            temp = null;
            
            // The drag image is created and drawn here.
            BufferedImage dragImage = new BufferedImage(imagePanel.getWidth(),
                imagePanel.getHeight(),
                BufferedImage.TYPE_INT_RGB);
            
            imagePanel.paint(dragImage.getGraphics());
            imagePanel = null;
            
            // This is the startDrag method to call. My subclass of JTree is a
            // DragSourceListener. You'll have to play with the offset point to
            
            // get the exact behavior you want. It sets where in the created image
            // the drop zone should be.
            DragSource.getDefaultDragSource().startDrag(event,
                DragSource.DefaultMoveDrop, dragImage, new Point(-8,-8),
                (Transferable) null /*transferable*/, this);
        }

        public void dragEnter(DragSourceDragEvent dsde) {
        }

        public void dragOver(DragSourceDragEvent dsde) {
        }

        public void dropActionChanged(DragSourceDragEvent dsde) {
        }

        public void dragExit(DragSourceEvent dse) {
        }

        public void dragDropEnd(DragSourceDropEvent dsde) {
        }
    }
    
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
        int row = getRowForLocation(e.getX(), e.getY());
        
        if (row < 0) {
            grabFocus();
            getSelectionModel().clearSelection();
            
            return;
        }
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
}
