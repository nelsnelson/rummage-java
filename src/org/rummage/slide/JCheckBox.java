/*
 * JCheckBox.java
 *
 * Created on May 11, 2006, 10:14 AM
 */

package org.rummage.slide;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Action;
import javax.swing.Icon;

/**
 *
 * @author nnelson
 */
public class JCheckBox
    extends javax.swing.JCheckBox
{
    /** Creates a new instance of JCheckBox */
    public JCheckBox() {
        super();
        setStandardProperties();
    }
    
    /** Creates a new instance of JCheckBox */
    public JCheckBox(Action action) {
        super(action);
        setStandardProperties();
    }
    
    /** Creates a new instance of JCheckBox */
    public JCheckBox(Icon icon) {
        super(icon);
        setStandardProperties();
    }
    
    /** Creates a new instance of JCheckBox */
    public JCheckBox(String text) {
        super(text);
        setStandardProperties();
    }
    
    /** Creates a new instance of JCheckBox */
    public JCheckBox(Icon icon, boolean selected) {
        super(icon, selected);
        setStandardProperties();
    }
    
    /** Creates a new instance of JCheckBox */
    public JCheckBox(String text, Icon icon) {
        super(text, icon);
        setStandardProperties();
    }
    
    /** Creates a new instance of JCheckBox */
    public JCheckBox(String text, boolean selected) {
        super(text, selected);
        setStandardProperties();
    }
    
    /** Creates a new instance of JCheckBox */
    public JCheckBox(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
        setStandardProperties();
    }
    
    private void setStandardProperties() {
        setOpaque(false);
        setFocusable(false);
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
}
