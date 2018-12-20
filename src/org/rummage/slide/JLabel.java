/*
 * JLabel.java
 *
 * Created on February 28, 2006, 3:39 PM
 */

package org.rummage.slide;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

/**
 *
 * @author nelsnelson
 */
public class JLabel
    extends javax.swing.JLabel
{
    /** Creates a new instance of JLabel */
    public JLabel() {
        super();
    }
    
    /** Creates a new instance of JLabel */
    public JLabel(String text) {
        super(text);
    }
    
    /** Creates a new instance of JLabel */
    public JLabel(Icon icon) {
        super(icon);
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
