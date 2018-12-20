/*
 * JTable.java
 *
 * Created on November 10, 2005, 4:44 PM
 */

package org.rummage.slide;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.rummage.slide.border.Background;
import org.rummage.slide.context.ContextManager;
import org.rummage.slide.dnd.AutoscrollSupport;
import org.rummage.slide.dnd.DragAndDropable;
import org.rummage.slide.dnd.DropAdapter;
import org.rummage.slide.table.ColumnFitAdapter;
import org.rummage.slide.table.TableColumnChooser;
import org.rummage.slide.table.TableColumnResizer;
import org.rummage.slide.table.TableTransferHandler;
import org.rummage.toolbox.util.Text;

/**
 *
 * @author not attributable
 */
public class JTable
    extends javax.swing.JTable
    implements Autoscroll, DragAndDropable
{
    /** Creates a new instance of JTable */
    public JTable() {
        super();
        setStandardProperties();
    }
    
    /** Creates a new instance of JTable */
    public JTable(int numRows, int numColumns) {
        super(numRows, numColumns);
        setStandardProperties();
    }
    
    /** Creates a new instance of JTable */
    public JTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
        setStandardProperties();
    }
    
    /** Creates a new instance of JTable */
    public JTable(Vector rowData, Vector columnNames) {
        super(rowData, columnNames);
        setStandardProperties();
    }
    
    /** Creates a new instance of JTable */
    public JTable(TableModel dm) {
        super(dm);
        setStandardProperties();
    }
    
    /** Creates a new instance of JTable */
    public JTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
        setStandardProperties();
    }
    
    /** Creates a new instance of JTable */
    public JTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
        setStandardProperties();
    }
    
    /** Creates a new instance of JTable */
    public JTable(String... columnNames) {
        this(new Text.Vector(columnNames));
    }
    
    /** Creates a new instance of JTable */
    public JTable(Vector columnNames) {
        super(new DefaultTableModel(new Vector(), columnNames));
        setStandardProperties();
    }
    
    public void setStandardProperties() {
        setAutoCreateColumnsFromModel(true);
        TableColumnChooser.install(this);
        getTableHeader().addMouseListener(new ColumnFitAdapter()); 
        setShowGrid(false);
        
        setDefaultEditor(Object.class, new SimpleTableCellEditor());
        
        SlideUtilities.enableEasyCellEditorReleasing(this);
        //SlideUtilities.enableSingleClickCellEditing(this);
        
        setBackground(new Background(this));
        
        new TableColumnResizer(this);
        
        if (getDragEnabled()) applyDNDHack();
    }
    
    public void clear() {
        DefaultTableModel model = (DefaultTableModel) getModel();

        model.setRowCount(0);
    }
    
    public Object getDataValueAt(int row, int column) {
        return getModel().getValueAt(row, column);
    }

    public void setDataValueAt(Object aValue, int row, int column) {
        getModel().setValueAt(aValue, row, column);
    }
    
    public void setRowData(Vector tableData) {
        clear();
        
        DefaultTableModel model = (DefaultTableModel) getModel();
        
        for (Vector row : (Vector<Vector>) tableData) {
            model.addRow(row);
        }
    }
    
    public void addRow(Object... rowData) {
        addRow(Text.vectorize(rowData));
    }
    
    public void addRow(Vector rowData) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        
        model.addRow(rowData);
    }
    
    public void insertRow(int index, Object... rowData) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        
        model.insertRow(index, rowData);
    }
    
    public void insertRow(int index, Vector rowData) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        
        model.insertRow(index, rowData);
    }
    
    public void removeRow(Object o) {
        removeRow(o, false);
    }
    
    public void removeRow(Object o, boolean rememberingSelection) {
        int row = getRowByValue(o);
        
        if (row < 0) {
            return;
        }
        
        DefaultTableModel model = (DefaultTableModel) getModel();
        
        model.removeRow(row);
        
        if (rememberingSelection) {
            if (row < getRowCount()) {
                setSelectedRow(row);
            }
            else if (getRowCount() > 0) {
                setSelectedRow(Math.max(0, row - 1));
            }
        }
    }

    public Vector getRow(int i) {
        if (i < 0) {
            return null;
        }
        
        DefaultTableModel model = (DefaultTableModel) getModel();
        
        return (Vector) model.getDataVector().get(i);
    }
    
    public int getRowByValue(Object o) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        
        Vector dataVector = model.getDataVector();
        
        for (int i = 0; i < dataVector.size(); i++) {
            Vector rowData = (Vector) dataVector.get(i);
            
            if (rowData.contains(o)) {
                return i;
            }
        }
        
        return -1;
    }

    public void setSelectedRow(int i) {
        getSelectionModel().setSelectionInterval(i, i);
    }
    
    private boolean editable = true;
    
    public void setEditable(boolean b) {
        editable = b;
    }
    
    public boolean isEditable() {
        return editable;
    }
    
    public boolean isCellEditable(int row, int column) {
        return
            isEditable() && getModel().isCellEditable(row,
            convertColumnIndexToModel(column));
    }
    
    public void hideColumns(int... columnNumbers) {
        TableColumnChooser.hideColumns(getColumnModel(), columnNumbers);
    }
    
    /**
     * Maps the index of the column in the view at
     * <code>viewColumnIndex</code> to the index of the column
     * in the table model.  Returns the index of the corresponding
     * column in the model.  If <code>viewColumnIndex</code>
     * is less than zero, returns <code>viewColumnIndex</code>.
     *
     * @param   viewColumnIndex     the index of the column in the view
     * @return  the index of the corresponding column in the model
     *
     * @see #convertColumnIndexToView
     */
    public int convertColumnIndexToModel(int viewColumnIndex) {
        if (viewColumnIndex < 0 || !TableColumnChooser.
            isVisibleColumn(getColumnModel(), viewColumnIndex))
        {
            return viewColumnIndex;
        }
        return getColumnModel().getColumn(viewColumnIndex).getModelIndex();
    }

    /**
     * Maps the index of the column in the table model at
     * <code>modelColumnIndex</code> to the index of the column
     * in the view.  Returns the index of the
     * corresponding column in the view; returns -1 if this column is not
     * being displayed.  If <code>modelColumnIndex</code> is less than zero,
     * returns <code>modelColumnIndex</code>.
     *
     * @param   modelColumnIndex     the index of the column in the model
     * @return   the index of the corresponding column in the view
     *
     * @see #convertColumnIndexToModel
     */
    public int convertColumnIndexToView(int modelColumnIndex) {
        if (modelColumnIndex < 0 || !TableColumnChooser.
            isVisibleColumn(getColumnModel(), modelColumnIndex))
        {
            return modelColumnIndex;
        }
        TableColumnModel cm = getColumnModel();
        for (int column = 0; column < getColumnCount(); column++) {
            if (cm.getColumn(column).getModelIndex() == modelColumnIndex) {
                return column;
            }
        }
        return -1;
    }
 
    public void setColumnsAlwaysVisible(int... columnNumbers) {
        putClientProperty(TableColumnChooser.FIXED_COLUMNS,
            (int[]) columnNumbers);
    }
    
    public void setPreferredColumnWidth(int columnNumber, int width) {
        getColumnModel().getColumn(columnNumber).setPreferredWidth(width);
    }
    
    public void setPreferredColumnWidths(int... columnWidths) {
        int i = 0; for (int width : columnWidths) {
            if (width > 0) setPreferredColumnWidth(i++, width);
        }
    }
    
    private boolean shaded = false;
    
    private Color defaultGridColor = ColorUtilities.getTranslucent(Color.LIGHT_GRAY, 0.40d);
    private Color shadedColor = ColorUtilities.getTranslucent(new Color(235, 235, 160), 0.40d);
    private Color unshadedColor = ColorUtilities.seeThrough();
    private Color selectedColor = new Color(100, 100, 235);
    private Color defaultSelectionBackgroundColor =
        Color.getColor(null, super.getSelectionBackground());
    //    ColorUtilities.getTranslucent(Color.getColor(null, super.getSelectionBackground()), 0.90d);
    private Color defaultSelectionForegroundColor =
        Color.getColor(null, super.getSelectionForeground());
    //    ColorUtilities.getTranslucent(Color.getColor(null, super.getSelectionForeground()), 0.90d);
    
    public void setRowsAlternatelyShaded(boolean shaded) {
        this.shaded = shaded;
    }
    
    public boolean isShadingAlternateRows() {
        return shaded;
    }
    
    public boolean isRowShaded(int row) {
        return row % 2 == 0;
    }
 
    public void setShadedRowColor(Color shadedColor) {
        this.shadedColor = shadedColor;
    }
 
    public void setUnhadedRowColor(Color unshadedColor) {
        this.unshadedColor = unshadedColor;
    }
 
    public Color getShadedRowColor() {
        return shadedColor;
    }
 
    public Color getUnshadedRowColor() {
        return unshadedColor;
    }
 
    public Color getDefaultGridColor() {
        return defaultGridColor;
    }
    
    java.awt.Dimension oldSpacing = null;
    
    public void setShowGrid(boolean b) {
        if (b) {
            // intercell spacing
            if (oldSpacing == null) {
                setIntercellSpacing(new java.awt.Dimension(1, 1));
            }
            else {
                setIntercellSpacing(oldSpacing);
            }
            
            Color gridColor = getDefaultGridColor();
            
            if (gridColor != null) {
                setGridColor(gridColor);
            }
        }
        else {
            // no intercell spacing
            if (oldSpacing == null) {
                oldSpacing = getIntercellSpacing();
            }
            
            setIntercellSpacing(new java.awt.Dimension(0, 0));
        }
        
        super.setShowGrid(b);
    }
    
    private boolean shouldAntialiasText = false;
    
    public void setAntialiasText(boolean shouldAntialiasText) {
        this.shouldAntialiasText = shouldAntialiasText;
    }

    private class SimpleTableCellEditor
        extends DefaultCellEditor
    {
        public SimpleTableCellEditor() {
            super(new JTextField() {
                public void paint(Graphics g) {
                    if (shouldAntialiasText) {
                        Graphics2D g2 = (Graphics2D) g;

                        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                                            RenderingHints.VALUE_RENDER_QUALITY);

                        super.paint(g2);

                        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                            RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                    }
                    else {
                        super.paint(g);
                    }
                }
            });
            
            Component c = getComponent();
            
            if (c instanceof JComponent) {
                final JTextField textField = (JTextField)c;
                
                textField.setBorder(BorderFactory.
                    createLineBorder(Color.LIGHT_GRAY, 1));
                
                textField.setEditable(editable);
                
                delegate = new EditorDelegate() {
                    public void setValue(Object value) {
                        if (textField.isEditable()) {
                            ContextManager.pauseUndoManager(textField);
                            
                            textField.setText((value != null) ?
                                value.toString() : "");
                            
                            ContextManager.resumeUndoManager(textField);
                        }
                    }
                    
                    public Object getCellEditorValue() {
                        return textField.getText();
                    }
                };
            }
        }
        
        /** Implements the <code>TableCellEditor</code> interface. */
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected,
                                                     int row, int column) {
            delegate.setValue(value);
            return editorComponent;
        }
    }
    
    public void paint(Graphics g) {
        if (shouldAntialiasText) {
            Graphics2D g2 = (Graphics2D) g;
            
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                                RenderingHints.VALUE_RENDER_QUALITY);
            
            super.paint(g2);
            
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
        else {
            super.paint(g);
        }
    }
    
    private Background background = null;
    private ImageIcon backgroundImage = null;
    private boolean repeatingBackground = false;

    private void setBackground(Background background) {
        this.background = background;
    }
    
    public void setBackgroundImage(ImageIcon image) {
        background.setBackgroundImage(image);
    }
    
    public boolean isRepeatingBackground() {
        return repeatingBackground;
    }
    
    public void setRepeatingBackground(boolean b) {
        background.setRepeatingBackground(b);
    }
    
    public void dropped(Object data, Point p) {}
    
    public void setDragAndDropPartner(Component partner) {
        setDropTarget(new DropTarget(partner, new DropAdapter()));
    }
    
    public void setDraggingOver(Point p) {
        if (p == null) {
            clearSelection();
            setSelectionBackground(defaultSelectionBackgroundColor);
            setSelectionForeground(defaultSelectionForegroundColor);
        }
        else {
            int row = rowAtPoint(p);

            if (row >= 0 && row < getRowCount() && row != getSelectedRow())
            {
                clearSelection();
                
                Color background =
                    getCellRenderer(row, 0).
                    getTableCellRendererComponent(this, getCellRenderer(row,
                    0), false, false, row, 0).getBackground();
                Color foreground =
                    getCellRenderer(row, 0).
                    getTableCellRendererComponent(this, getCellRenderer(row,
                    0), false, false, row, 0).getForeground();
                
                addRowSelectionInterval(row, row); // row your boat
            }
        }
    }
    
    /*
    public TableCellEditor getCellEditor() {
        TableCellEditor editor = super.getCellEditor();
        
        int row = getSelectedRow();
        int column = getSelectedColumn();
        
        Component c =
            editor.getTableCellEditorComponent(this, getValueAt(row, column),
            true, row, column);
        
        shadeIfNecessary(c, row, column);
        
        return editor;
    }
    
    public TableCellEditor getCellEditor(int row, int column) {
        TableCellEditor editor = super.getCellEditor(row, column);
        
        Component c =
            editor.getTableCellEditorComponent(this, getValueAt(row, column),
            true, row, column);
        
        shadeIfNecessary(c, row, column);
        
        return editor;
    }
     */
    
    public Component prepareEditor(TableCellEditor editor, int row,
        int column)
    {
        Component c =
            editor.getTableCellEditorComponent(this, getValueAt(row, column),
            true, row, column);
        
        c.setBackground(getSpecialBackgroundMaybe(row, column));
        
        return c;
    }
    
    public Component prepareRenderer(TableCellRenderer renderer, int row,
        int column)
    {
        Component c = super.prepareRenderer(renderer, row, column);
        
        c.setBackground(getSpecialBackgroundMaybe(row, column));
        
        return c;
    }
    
    public Color getSpecialBackgroundMaybe(int row, int column) {
        if (shaded) {
            if (isCellSelected(row, column)) {
                return getSelectionBackground();
            }
            else if (isRowShaded(row)) {
                return getShadedRowColor();
            }
            else {
                return getUnshadedRowColor();
            }
        }
        
        return getBackground();
    }
 
    // overriden to make the height of scroll match viewpost height if smaller
    public boolean getScrollableTracksViewportHeight() {
        return getPreferredSize().height < getParent().getHeight();
    }
    
    public int getScrollableUnitIncrement(Rectangle visibleRect,
        int orientation, int direction)
    {
        return getFontMetrics(getFont()).getHeight() + 25;
    }
    
    public int getScrollableBlockIncrement(Rectangle visibleRect,
        int orientation, int direction)
    {
        return getFontMetrics(getFont()).getHeight() + 25;
    }
 
    private AutoscrollSupport scrollSupporter = null;
    
    public AutoscrollSupport getScrollSupporter() {
        if (scrollSupporter == null) {
            scrollSupporter = new AutoscrollSupport(this);
        }
        
        return scrollSupporter;
    }
 
    public Insets getAutoscrollInsets(){ 
        return getScrollSupporter().getAutoscrollInsets(); 
    } 
 
    public void autoscroll(Point cursorLocn){ 
        getScrollSupporter().autoscroll(cursorLocn); 
    } 
    
    public void setDragEnabled(boolean b) {
        super.setDragEnabled(b);
        
        if (b) applyDNDHack();
        else disableDNDHack();
    }
    
    private MouseListener dragListener = null; 
    
    public void applyDNDHack(){ 
        // the default dnd implemntation requires to first select node and then drag 
        try{ 
            Class clazz = Class.forName("javax.swing.plaf.basic.BasicDragGestureRecognizer"); 
            MouseListener[] mouseListeners = getMouseListeners(); 
            for(int i = 0; i<mouseListeners.length; i++){ 
                if(clazz.isAssignableFrom(mouseListeners[i].getClass())){ 
                    dragListener = mouseListeners[i]; 
                    break; 
                } 
            } 

            if(dragListener!=null){ 
                removeMouseListener(dragListener); 
                removeMouseMotionListener((MouseMotionListener)dragListener); 
                addMouseListener(dragListener); 
                addMouseMotionListener((MouseMotionListener)dragListener); 
                setTransferHandler(new TableTransferHandler());
            } 
        } catch(ClassNotFoundException e){ 
            e.printStackTrace();    
        } 
    } 
    
    public void disableDNDHack(){
        if(dragListener!=null){ 
            removeMouseListener(dragListener); 
            removeMouseMotionListener((MouseMotionListener)dragListener); 
        }
        
        setDragAndDropPartner(null);
        setDropTarget(null);
        dragListener = null;
    }
    
    public Object[] getSelectedValuesInColumn(int column) {
        Vector values = new Vector();
        int[] selectedRows = getSelectedRows();
        
        for (int row : selectedRows) {
            values.add(getValueAt(row, column));
        }
        
        return values.toArray();
    }

    public int getColumnIndex(String text) {
        return getColumnModel().getColumnIndex(text);
    }
}
