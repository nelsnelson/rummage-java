/*
 * JList.java
 *
 * Created on May 2, 2006, 1:39 PM
 */
package org.rummage.slide;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;

/**
 *
 * @author nnelson
 */
public class JList
    extends javax.swing.JList
{
    /** Creates a new instance of JList */
    public JList() {
        super(new DefaultListModel());
        
        setSelectionMode(DefaultListSelectionModel.
            MULTIPLE_INTERVAL_SELECTION);
    }
    
    /** Creates a new instance of JList */
    public JList(Vector listData) {
        super(listData);
        
        setSelectionMode(DefaultListSelectionModel.
            MULTIPLE_INTERVAL_SELECTION);
    }
    
    public void addItem(int index, Object o) {
        ((DefaultListModel) getModel()).add(index, o);
    }
    
    public void addItem(Object o) {
        ((DefaultListModel) getModel()).addElement(o);
    }
    
    public Object get(int index) {
        return ((DefaultListModel) getModel()).get(index);
    }
    
    private boolean shouldAntialiasText = false;
    
    public void setAntialiasText(boolean shouldAntialiasText) {
        this.shouldAntialiasText = shouldAntialiasText;
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
    
    private boolean shaded = false;
    
    private Color shadedColor = new Color(250, 250, 230);
    private Color unshadedColor = new Color(255, 255, 255);
    private Color selectedColor = new Color(100, 100, 235);
    private Color defaultSelectionBackgroundColor =
        Color.getColor(null, super.getSelectionBackground());
    private Color defaultSelectionForegroundColor =
        Color.getColor(null, super.getSelectionForeground());
    
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
 
    public Color getShadedRowColor() {
        return shadedColor;
    }
}
