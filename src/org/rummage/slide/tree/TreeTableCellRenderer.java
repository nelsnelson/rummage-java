/*
 * TreeTableCellRenderer.java
 *
 * Created on November 8, 2005, 10:49 AM
 */

package org.rummage.slide.tree;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.rummage.slide.ColorUtilities;
import org.rummage.slide.JTree;
import org.rummage.slide.JTreeTable;

/**
 * A TreeTableCellRenderer that displays a JTree.
 *
 * @author nelsnelson
 */
public class TreeTableCellRenderer
    extends JTree
    implements TableCellRenderer
{
    JTreeTable treeTable;
    
    /** Last table/tree row asked to renderer. */
    protected int visibleRow;

    public TreeTableCellRenderer(TreeModel model,
        JTreeTable treeTable)
    {
        super(model);
        this.treeTable = treeTable;
    }

    /**
     * updateUI is overridden to set the colors of the Tree's renderer
     * to match that of the table.
     */
    public void updateUI() {
        super.updateUI();
        // Make the tree's cell renderer use the table's cell selection
        // colors.
        TreeCellRenderer tcr = getCellRenderer();
        if (tcr instanceof DefaultTreeCellRenderer) {
            DefaultTreeCellRenderer dtcr = ((DefaultTreeCellRenderer)tcr);
            // For 1.1 uncomment this, 1.2 has a bug that will cause an
            // exception to be thrown if the border selection color is
            // null.
            // dtcr.setBorderSelectionColor(null);
        }
    }

    /**
     * Sets the row height of the tree, and forwards the row height to
     * the table.
     */
    public void setRowHeight(int rowHeight) {
        if (rowHeight > 0) {
            super.setRowHeight(rowHeight);
            if (treeTable != null &&
                treeTable.getRowHeight() != rowHeight) {
                treeTable.setRowHeight(getRowHeight());
            }
        }
    }

    /**
     * This is overridden to set the height to match that of the JTable.
     */
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, 0, w, treeTable.getHeight());
    }

    /**
     * Subclassed to translate the graphics such that the last visible
     * row will be drawn at 0,0.
     */
    public void paint(Graphics g) {
        g.translate(0, -visibleRow * getRowHeight());
        super.paint(g);
    }

    /**
     * TableCellRenderer method. Overridden to update the visible row.
     */
    public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column)
    {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        }
        else {
            setBackground(ColorUtilities.seeThrough());
        }
        
        TreeCellRenderer tcr = getCellRenderer();
        if (tcr instanceof DefaultTreeCellRenderer) {
            DefaultTreeCellRenderer dtcr = ((DefaultTreeCellRenderer) tcr);
            if (isSelected) {
                dtcr.setBackgroundSelectionColor(table.getSelectionBackground());
                dtcr.setTextNonSelectionColor(table.getSelectionForeground());
            }
            else {
                dtcr.setBackgroundNonSelectionColor(getBackground());
                //dtcr.setBackgroundNonSelectionColor(treeTable.getSpecialBackgroundMaybe(row, column));
                dtcr.setTextNonSelectionColor(treeTable.getForeground());
            }
        }
        
        visibleRow = row;
        return this;
    }
}
