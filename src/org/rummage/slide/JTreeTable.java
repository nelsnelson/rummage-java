/*
 * @(#)JTreeTable.java  1.2 98/10/27
 *
 * Copyright 1997, 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package org.rummage.slide;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.rummage.slide.table.TableColumnResizer;
import org.rummage.slide.tree.AbstractCellEditor;
import org.rummage.slide.tree.DefaultTreeTableModel;
import org.rummage.slide.tree.TreeTableCellRenderer;
import org.rummage.slide.tree.TreeTableModel;
import org.rummage.slide.tree.TreeTableModelAdapter;
import org.rummage.toolbox.util.Text;
import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;

/**
 * A simple JTreeTable component, using a JTree as a renderer (and editor) for
 * the cells in a particular column in the JTable.
 *
 * @version 1.2 10/27/98
 *
 * @author Philip Milne
 * @author Scott Violet
 */
public class JTreeTable
    extends JTable
{
    /** A subclass of JTree. */
    protected TreeTableCellRenderer tree;
    
    /** Creates a new instance of JTreeTable */
    public JTreeTable() {
        this(new DefaultTreeTableModel());
    }
    
    /** Creates a new instance of JTreeTable */
    public JTreeTable(String... columnNames) {
        this(new Text.Vector(columnNames));
    }
    
    /** Creates a new instance of JTreeTable */
    public JTreeTable(Vector columnNames) {
        this(new DefaultTreeTableModel(columnNames));
    }
    
    /** Creates a new instance of JTreeTable */
    public JTreeTable(TreeTableModel treeTableModel) {
        super();
        
        tree = new TreeTableCellRenderer(treeTableModel, this);
        
        // Install a tableModel representing the visible rows in the tree.
        setModel(treeTableModel);
        
        setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        
        // No grid.
        setShowGrid(false);
        
        // No intercell spacing
        setIntercellSpacing(new Dimension(0, 0));
        
        // And update the height of the trees row to match that of
        // the table.
        if (tree.getRowHeight() < 1) {
            setRowHeight(22);
        }
    }
    
    public void setModel(TreeTableModel treeTableModel) {
        // Create the tree. It will be used as a renderer and editor.
        setCellRenderer(new TreeTableCellRenderer(treeTableModel, this));
        
        // Install a tableModel representing the visible rows in the tree.
        super.setModel(new TreeTableModelAdapter(treeTableModel, tree));
        
        // Force the JTable and JTree to share their row selection models.
        ListToTreeSelectionModelWrapper selectionWrapper =
            new ListToTreeSelectionModelWrapper();
        
        tree.setSelectionModel(selectionWrapper);
        setSelectionModel(selectionWrapper.getListSelectionModel());
        
        // Install the tree editor renderer and editor.
        setDefaultRenderer(TreeTableModel.class, tree);
        setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());
        
        expandAllRows();
    }
    
    public TreeTableModelAdapter getAdapter() {
        TableModel model = super.getModel();
        
        if (model instanceof TreeTableModelAdapter) {
            return (TreeTableModelAdapter) model;
        }
        else {
            return null;
        }
    }
    
    public Object getRoot() {
        return tree.getModel().getRoot();
    }
    
    public void clear() {
        setModel(new DefaultTreeTableModel());
    }
    
    //
    // Adding and removing columns in the view
    //
    
    public void addColumns(String... columnNames) {
        ((DefaultTreeTableModel) getAdapter().getModel()).addColumns(columnNames);
    }

    /**
     *  Appends <code>aColumn</code> to the end of the array of columns held by
     *  this <code>JTable</code>'s column model.
     *  If the column name of <code>aColumn</code> is <code>null</code>,
     *  sets the column name of <code>aColumn</code> to the name
     *  returned by <code>getModel().getColumnName()</code>.
     *  <p>
     *  To add a column to this <code>JTable</code> to display the
     *  <code>modelColumn</code>'th column of data in the model with a
     *  given <code>width</code>, <code>cellRenderer</code>,
     *  and <code>cellEditor</code> you can use:
     *  <pre>
     *
     *      addColumn(new TableColumn(modelColumn, width, cellRenderer, cellEditor));
     *
     *  </pre>
     *  [Any of the <code>TableColumn</code> constructors can be used
     *  instead of this one.]
     *  The model column number is stored inside the <code>TableColumn</code>
     *  and is used during rendering and editing to locate the appropriates
     *  data values in the model. The model column number does not change
     *  when columns are reordered in the view.
     *
     *  @param  aColumn         the <code>TableColumn</code> to be added
     *  @see    #removeColumn
     */
    public void addColumn(TableColumn aColumn) {
        TreeTableModelAdapter adapter = getAdapter();
        
        if (adapter != null) {
        DefaultTreeTableModel m = (DefaultTreeTableModel) adapter.getModel();
        if (aColumn.getHeaderValue() == null) {
	    int modelColumn = aColumn.getModelIndex();
	    String columnName = m.getColumnName(modelColumn);
            aColumn.setHeaderValue(columnName);
        }
        getColumnModel().addColumn(aColumn);
        }
    }
    
    /**
     * Creates default columns for the table from
     * the data model using the <code>getColumnCount</code> method
     * defined in the <code>TableModel</code> interface.
     * <p>
     * Clears any existing columns before creating the
     * new columns based on information from the model.
     *
     * @see     #getAutoCreateColumnsFromModel
     */
    public void createDefaultColumnsFromModel() {
        TreeTableModelAdapter adapter = getAdapter();
        
        if (adapter != null) {
        DefaultTreeTableModel m = (DefaultTreeTableModel) adapter.getModel();
        TableColumnModel cm = getColumnModel();
        if (m != null && cm.getColumnCount() == 0) {

            // Create new columns from the data model info
            for (int i = 0; i < m.getColumnCount(); i++) {
                TableColumn newColumn = new TableColumn(i);
                newColumn.setHeaderValue(m.getColumnName(i));
                addColumn(newColumn);
            }
        }
        }
    }
    
    /**
     * Overridden to message super and forward the method to the tree.
     * Since the tree is not actually in the component hieachy it will
     * never receive this unless we forward it in this manner.
     */
    public void updateUI() {
        super.updateUI();
        
        if (tree != null) {
            tree.updateUI();
        }
        
        // Use the tree's default foreground and background colors in the
        // table.
        LookAndFeel.installColorsAndFont(this, "Tree.background",
            "Tree.foreground", "Tree.font");
    }
    
    public void expandAllRows() {
        tree.expandAllRows();
    }
    
    public void collapseAllRows() {
        tree.collapseAllRows();
    }
    
    public void expandAllNodes() {
        tree.expandAllNodes(-1);
    }
    
    /**
     * Workaround for BasicTableUI anomaly. Make sure the UI never tries to
     * paint the editor. The UI currently uses different techniques to
     * paint the renderers and editors and overriding setBounds() below
     * is not the right thing to do for an editor. Returning -1 for the
     * editing row in this case, ensures the editor is never painted.
     */
    public int getEditingRow() {
        return
            getColumnClass(editingColumn) == TreeTableModel.class ? -1 :
            editingRow;
    }
    
    public int getRowCount() {
        return tree.getRowCount();
    }
    
    public void expandRow(int row) {
        tree.expandRow(row);
    }
    
    public TreePath getPathForRow(int row) {
        return tree.getPathForRow(row);
    }
    
    public TreeNode getNodeForRow(int row) {
        TreePath treePath = tree.getPathForRow(row);
        
        return (TreeNode) treePath.getPathComponent(treePath.getPathCount() - 1);
    }
    
    public TreePath getSelectionPath() {
        return tree.getSelectionPath();
    }
    
    public TreeNode getSelectionNode() {
        TreePath treePath = tree.getSelectionPath();
        
        if (treePath == null) {
            return new DefaultMutableTreeNode(getRoot());
        }
        
        return new DefaultMutableTreeNode(treePath.getPathComponent(treePath.getPathCount() - 1));
    }
    
    /** 
     * Gets the <code>DefaultMutableTreeNode</code> object from the 
     * <code>TreePath</code>.
     * 
     * @param  path  The <code>TreePath</code> from which to get the 
     *               <code>TreeNode</code>.
     * @return       The <code>DefaultMutableTreeNode</code> obtained from the 
     *               <code>TreePath</code> by calling 
     *               <code>getLastPathComponent()</code>. 
     */
    public DefaultMutableTreeNode getTreeNode(TreePath path) {
        return
            path == null ? null :
                new DefaultMutableTreeNode(path.getLastPathComponent());
    }
    
    public String getNodeName(DefaultMutableTreeNode node) {
        Object o = node.getUserObject();
        
        if (o instanceof ElementNSImpl) {
            ElementNSImpl element = (ElementNSImpl) o;
            
            return element.getLocalName();
        }
        
        return "";
    }
    
    /**
     * Overridden to pass the new rowHeight to the tree.
     */
    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);
        if (tree != null && tree.getRowHeight() != rowHeight) {
            tree.setRowHeight(getRowHeight());
        }
    }
    
    /**
     * Returns the tree that is being shared between the model.
     */
    public JTree getTree() {
        return tree;
    }
    
    public void setCellRenderer(TreeCellRenderer treeCellRenderer) {
        tree.setCellRenderer(treeCellRenderer);
        setDefaultRenderer(getAdapter().getClass(), tree);
    }
    
    public void setCellRenderer(TreeTableCellRenderer treeTableCellRenderer) {
        treeTableCellRenderer.setCellRenderer(tree.getCellRenderer());
        tree = treeTableCellRenderer;
    }
    
    // mouse press intended for resize shouldn't change row/col/cell selection
    public void changeSelection(int row, int column, boolean toggle,
        boolean extend)
    {
        if (getCursor() == TableColumnResizer.resizeCursor) {
            return;
        }
        
        super.changeSelection(row, column, toggle, extend);
    }
    
    public boolean editCellAt(int row, int column, EventObject e){
        if(e instanceof MouseEvent){
            MouseEvent me = (MouseEvent)e;
            // If the modifiers are not 0 (or the left mouse button),
            // tree may try and toggle the selection, and table
            // will then try and toggle, resulting in the
            // selection remaining the same. To avoid this, we
            // only dispatch when the modifiers are 0 (or the left mouse
            // button).
            if(me.getModifiers()==0 ||
                me.getModifiers()==InputEvent.BUTTON1_MASK){
                for(int counter = getColumnCount()-1; counter>= 0;
                counter--){
                    if(getColumnClass(counter)==TreeTableModel.class){
                        MouseEvent newME = new MouseEvent
                            (tree, me.getID(),
                            me.getWhen(), me.getModifiers(),
                            me.getX()-getCellRect(0, counter, true).x,
                            me.getY(), me.getClickCount(),
                            me.isPopupTrigger());
                        tree.dispatchEvent(newME);
                        break;
                    }
                }
            }
            return false;
        }
        return super.editCellAt(row, column, e);
    }
    
    public TreeNode find(String text) {
        return find(text, Math.max(0, getSelectedRow()), Position.Bias.Forward);
    }
    
    public TreeNode find(String text, int startRow) {
        return find(text, startRow, Position.Bias.Forward);
    }
    
    public TreeNode find(String text, int startRow, Position.Bias bias) {
        return getTreeNode(tree.find(text, startRow, bias));
    }
    
    public void expandNode(TreeNode node) {
        TreePath path = tree.getPath(node);
        
        tree.expandPath(path);
    }
    
    public void setSelection(TreeNode node) {
        tree.setSelectionPath(tree.getPath(node));
    }
    
    /**
     * TreeTableCellEditor implementation. Component returned is the
     * JTree.
     */
    public class TreeTableCellEditor extends AbstractCellEditor implements
        TableCellEditor {
        public Component getTableCellEditorComponent(javax.swing.JTable table,
            Object value,
            boolean isSelected,
            int r, int c) {
            return tree;
        }
        
        /**
         * Overridden to return false, and if the event is a mouse event
         * it is forwarded to the tree.<p>
         * The behavior for this is debatable, and should really be offered
         * as a property. By returning false, all keyboard actions are
         * implemented in terms of the table. By returning true, the
         * tree would get a chance to do something with the keyboard
         * events. For the most part this is ok. But for certain keys,
         * such as left/right, the tree will expand/collapse where as
         * the table focus should really move to a different column. Page
         * up/down should also be implemented in terms of the table.
         * By returning false this also has the added benefit that clicking
         * outside of the bounds of the tree node, but still in the tree
         * column will select the row, whereas if this returned true
         * that wouldn't be the case.
         * <p>By returning false we are also enforcing the policy that
         * the tree will never be editable (at least by a key sequence).
         */
        public boolean isCellEditable(EventObject e) {
            if (e instanceof MouseEvent) {
                for (int counter = getColumnCount() - 1; counter >= 0;
                counter--) {
                    if (getColumnClass(counter) == TreeTableModel.class) {
                        MouseEvent me = (MouseEvent)e;
                        MouseEvent newME = new MouseEvent(tree, me.getID(),
                            me.getWhen(), me.getModifiers(),
                            me.getX() - getCellRect(0, counter, true).x,
                            me.getY(), me.getClickCount(),
                            me.isPopupTrigger());
                        tree.dispatchEvent(newME);
                        break;
                    }
                }
            }
            return false;
        }
    }
    
    /**
     * ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel
     * to listen for changes in the ListSelectionModel it maintains. Once
     * a change in the ListSelectionModel happens, the paths are updated
     * in the DefaultTreeSelectionModel.
     */
    class ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel {
        /** Set to true when we are updating the ListSelectionModel. */
        protected boolean         updatingListSelectionModel;
        
        public ListToTreeSelectionModelWrapper() {
            super();
            
            getListSelectionModel().setSelectionMode
                (JTreeTable.this.getSelectionModel().getSelectionMode());
            
            getListSelectionModel().addListSelectionListener
                (createListSelectionListener());
        }
        
        /**
         * Returns the list selection model. ListToTreeSelectionModelWrapper
         * listens for changes to this model and updates the selected paths
         * accordingly.
         */
        ListSelectionModel getListSelectionModel() {
            return listSelectionModel;
        }
        
        /**
         * This is overridden to set <code>updatingListSelectionModel</code>
         * and message super. This is the only place DefaultTreeSelectionModel
         * alters the ListSelectionModel.
         */
        public void resetRowSelection() {
            if(!updatingListSelectionModel) {
                updatingListSelectionModel = true;
                try {
                    super.resetRowSelection();
                } finally {
                    updatingListSelectionModel = false;
                }
            }
            // Notice how we don't message super if
            // updatingListSelectionModel is true. If
            // updatingListSelectionModel is true, it implies the
            // ListSelectionModel has already been updated and the
            // paths are the only thing that needs to be updated.
        }
        
        /**
         * Creates and returns an instance of ListSelectionHandler.
         */
        protected ListSelectionListener createListSelectionListener() {
            return new ListSelectionHandler();
        }
        
        /**
         * If <code>updatingListSelectionModel</code> is false, this will
         * reset the selected paths from the selected rows in the list
         * selection model.
         */
        protected void updateSelectedPathsFromSelectedRows() {
            if(!updatingListSelectionModel) {
                updatingListSelectionModel = true;
                try {
                    // This is way expensive, ListSelectionModel needs an
                    // enumerator for iterating.
                    int        min = listSelectionModel.getMinSelectionIndex();
                    int        max = listSelectionModel.getMaxSelectionIndex();
                    
                    clearSelection();
                    if(min != -1 && max != -1) {
                        for(int counter = min; counter <= max; counter++) {
                            if(listSelectionModel.isSelectedIndex(counter)) {
                                TreePath     selPath = tree.getPathForRow
                                    (counter);
                                
                                if(selPath != null) {
                                    addSelectionPath(selPath);
                                }
                            }
                        }
                    }
                } finally {
                    updatingListSelectionModel = false;
                }
            }
        }
        
        /**
         * Class responsible for calling updateSelectedPathsFromSelectedRows
         * when the selection of the list changse.
         */
        class ListSelectionHandler implements ListSelectionListener {
            public void valueChanged(ListSelectionEvent e) {
                // Ignore extra messages.
                if (e.getValueIsAdjusting()) {
                    return;
                }

                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()) {

                }
                else {
                    int selectedRow = lsm.getMinSelectionIndex();
                    tree.setSelectionRow(selectedRow);
                }
                
                updateSelectedPathsFromSelectedRows();
            }
        }
    }
}
