/*
 * TableTransferHandler.java
 *
 * Created on December 13, 2005, 11:40 AM
 */

package org.rummage.slide.table;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableModel;

import org.rummage.slide.JTable;
import org.rummage.slide.tree.TreeTableModelAdapter;

public class TableTransferHandler
    extends StringTransferHandler
{
    private int[] rows = null;
    private int colCount = 0;
    private int addIndex = -1; //Location where items were added
    private int addCount = 0;  //Number of items added.

    protected String exportString(JComponent c) {
        JTable table = (JTable)c;
        StringBuffer buff = new StringBuffer();
        
        Object o = table.getModel();
        
        if (o instanceof DefaultTableModel) {
            DefaultTableModel model = (DefaultTableModel) o;
            rows = table.getSelectedRows();
            colCount = model.getColumnCount();
            
            for (int i = 0; i < rows.length; i++) {
                for (int j = 0; j < colCount; j++) {
                    Object val = model.getValueAt(i, j);
                    buff.append(val == null ? "" : val.toString());
                    if (j != colCount - 1) {
                        buff.append(",");
                    }
                }
                if (i != rows.length - 1) {
                    buff.append("\n");
                }
            }
        }
        else if (o instanceof TreeTableModelAdapter) {
            TreeTableModelAdapter adapter = (TreeTableModelAdapter) o;
            rows = table.getSelectedRows();
            colCount = adapter.getColumnCount();
            
            for (int i = 0; i < rows.length; i++) {
                for (int j = 0; j < colCount; j++) {
                    Object val = adapter.getValueAt(i, j);
                    buff.append(val == null ? "" : val.toString());
                    if (j != colCount - 1) {
                        buff.append(",");
                    }
                }
                if (i != rows.length - 1) {
                    buff.append("\n");
                }
            }
        }
        
        return buff.toString();
    }

    protected void importString(JComponent c, String str) {
        JTable target = (JTable)c;
        Object o = target.getModel();
        
        if (o instanceof DefaultTableModel) {
            DefaultTableModel model = (DefaultTableModel) o;
            int index = target.getSelectedRow();

            //Prevent the user from dropping data back on itself.
            //For example, if the user is moving rows #4,#5,#6 and #7 and
            //attempts to insert the rows after row #5, this would
            //be problematic when removing the original rows.
            //So this is not allowed.
            if (rows != null && index >= rows[0] - 1 &&
                  index <= rows[rows.length - 1]) {
                rows = null;
                return;
            }

            int max = model.getRowCount();
            if (index < 0) {
                index = max;
            } else {
                index++;
                if (index > max) {
                    index = max;
                }
            }
            addIndex = index;
            String[] values = str.split("\n");
            addCount = values.length;
            int colCount = target.getColumnCount();
            for (int i = 0; i < values.length && i < colCount; i++) {
                model.insertRow(index++, values[i].split(","));
            }
        }
        else if (o instanceof TreeTableModelAdapter) {
            TreeTableModelAdapter adapter = (TreeTableModelAdapter) o;
            int index = target.getSelectedRow();

            //Prevent the user from dropping data back on itself.
            //For example, if the user is moving rows #4,#5,#6 and #7 and
            //attempts to insert the rows after row #5, this would
            //be problematic when removing the original rows.
            //So this is not allowed.
            if (rows != null && index >= rows[0] - 1 &&
                  index <= rows[rows.length - 1]) {
                rows = null;
                return;
            }

            int max = adapter.getRowCount();
            if (index < 0) {
                index = max;
            } else {
                index++;
                if (index > max) {
                    index = max;
                }
            }
            addIndex = index;
            String[] values = str.split("\n");
            addCount = values.length;
            int colCount = target.getColumnCount();
            for (int i = 0; i < values.length && i < colCount; i++) {
                adapter.insertRow(index++, values[i].split(","));
            }
        }
    }
    
    protected void cleanup(JComponent c, boolean remove) {
        JTable source = (JTable)c;
        if (remove && rows != null) {
            Object o = source.getModel();
            
            if (o instanceof DefaultTableModel) {
                DefaultTableModel model = (DefaultTableModel) o;

                //If we are moving items around in the same table, we
                //need to adjust the rows accordingly, since those
                //after the insertion point have moved.
                if (addCount > 0) {
                    for (int i = 0; i < rows.length; i++) {
                        if (rows[i] > addIndex) {
                            rows[i] += addCount;
                        }
                    }
                }
                for (int i = rows.length - 1; i >= 0; i--) {
                    model.removeRow(rows[i]);
                }
            }
            else if (o instanceof TreeTableModelAdapter) {
                TreeTableModelAdapter adapter = (TreeTableModelAdapter) o;

                //If we are moving items around in the same table, we
                //need to adjust the rows accordingly, since those
                //after the insertion point have moved.
                if (addCount > 0) {
                    for (int i = 0; i < rows.length; i++) {
                        if (rows[i] > addIndex) {
                            rows[i] += addCount;
                        }
                    }
                }
                for (int i = rows.length - 1; i >= 0; i--) {
                    adapter.removeRow(rows[i]);
                }
            }
        }
        rows = null;
        addCount = 0;
        addIndex = -1;
    }
}

abstract class StringTransferHandler extends TransferHandler {
    
    protected abstract String exportString(JComponent c);
    protected abstract void importString(JComponent c, String str);
    protected abstract void cleanup(JComponent c, boolean remove);
    
    protected Transferable createTransferable(JComponent c) {
        return new StringSelection(exportString(c));
    }
    
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }
    
    public boolean importData(JComponent c, Transferable t) {
        if (canImport(c, t.getTransferDataFlavors())) {
            try {
                String str = (String)t.getTransferData(DataFlavor.stringFlavor);
                importString(c, str);
                return true;
            } catch (UnsupportedFlavorException ufe) {
            } catch (IOException ioe) {
            }
        }

        return false;
    }
    
    protected void exportDone(JComponent c, Transferable data, int action) {
        cleanup(c, action == MOVE);
    }
    
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        for (int i = 0; i < flavors.length; i++) {
            if (DataFlavor.stringFlavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
    }
}
