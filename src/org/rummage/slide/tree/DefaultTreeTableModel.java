/*
 * DefaultTreeTableModel.java
 *
 * Created on November 15, 2003, 06:22
 */

package org.rummage.slide.tree;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;


/**
 * JTreeTable data model that extends AbstractTreeTableModel.  Displays the
 * tree in a single column.  Override the getColumnCount and getValueAt
 * methods to display in multiple columns.
 *
 * @author nelsnelson
 */
public class DefaultTreeTableModel
    extends AbstractTreeTableModel
{
    protected Vector<Class> columnClasses = new Vector();
    protected Vector<String> columnIdentifiers = new Vector();
    
    /**
     * Creates a tree in which any node can have children.
     *
     * @see #DefaultTreeModel(TreeNode, boolean)
     */
    public DefaultTreeTableModel() {
        this(new DefaultMutableTreeNode());
    }
    
    /**
     * Creates a tree in which any node can have children.
     *
     * @param root an object that is the root of the tree
     * @see #DefaultTreeModel(TreeNode, boolean)
     */
    public DefaultTreeTableModel(Object root) {
        this((TreeNode) root, false);
    }
    
    /**
     * Creates a tree in which any node can have children.
     *
     * @param root a TreeNode object that is the root of the tree
     * @see #DefaultTreeModel(TreeNode, boolean)
     */
    public DefaultTreeTableModel(TreeNode root) {
        this(root, false);
    }
    
    public DefaultTreeTableModel(Vector columnNames) {
        this(new DefaultMutableTreeNode(), columnNames);
    }
    
    public DefaultTreeTableModel(Object root, Vector columnNames) {
        this((TreeNode) root, columnNames);
    }
    
    public DefaultTreeTableModel(TreeNode root, Vector columnNames) {
        this(root);
        
        addColumns(columnNames);
    }

    /**
     * Creates a tree specifying whether any node can have children,
     * or whether only certain nodes can have children.
     *
     * @param root a TreeNode object that is the root of the tree
     * @param asksAllowsChildren a boolean, false if any node can
     *        have children, true if each node is asked to see if
     *        it can have children
     * @see #asksAllowsChildren
     */
    public DefaultTreeTableModel(TreeNode root, boolean asksAllowsChildren) {
        super(root);
        
        addColumn(TreeTableModel.class, "Node");
        addColumn(String.class, "Value");
    }
    
    protected List getChildren(Object node) {
        TreeNode treeNode = ((TreeNode) node); 
        return Collections.list(treeNode.children());
    }
    
    //
    // The TreeModel interface
    //

    public int getChildCount(Object node) {
        return ((TreeNode) node).getChildCount();
    }
    
    public Object getChild(Object node, int i) { 
        return getChildren(node).get(i);
    }
    
    // TreeTable specific methods
    
    /**
     * Add more than one column at a time.  Only for Strings for right now
     * though.
     */
    public void addColumns(String... columnNames) {
        for (String columnName : columnNames) {
            addColumn(String.class, columnName);
        }
    }
    
    public void addColumns(Vector columnNames) {
        for (String columnName : (Vector<String>) columnNames) {
            addColumn(String.class, columnName);
        }
    }
    
    /** Add a column. */
    public void addColumn(Class columnClass, String columnName) {
        columnClasses.add(columnClass);
        columnIdentifiers.add(columnName);
    }
    
    /**
     * Returns the number of available columns.
     */
    public int getColumnCount() {
        return columnClasses.size();
    }

    /**
     * Returns the name for column number <code>column</code>.
     */
    public String getColumnName(int column) {
        return columnIdentifiers.get(column);
    }

    /**
     * Returns the type for column number <code>column</code>.
     */
    public Class getColumnClass(int column) {
        return columnClasses.get(column);
    }

    /**
     * Returns the value to be displayed for node <code>node</code>, 
     * at column number <code>column</code>.
     */
    public Object getValueAt(Object node, int column) {
        return column == 0 ? node : null;
    }

   /**
    * By default, make the column with the Tree in it the only editable one. 
    * Making this column editable causes the JTable to forward mouse 
    * and keyboard events in the Tree column to the underlying JTree. 
    */ 
    public boolean isCellEditable(Object node, int column) { 
         return getColumnClass(column) == TreeTableModel.class; 
    }

    /**
     * Sets the value for node <code>node</code>, 
     * at column number <code>column</code>.
     */
    public void setValueAt(Object aValue, Object node, int column) {
        
    }

    public void removeNode(Object node) {
        
    }
    
    public void insertNode(Object node, Object newNode) {
        
    }
}
