/*
 * JTextPane.java
 *
 * Created on January 18, 2006, 11:22 AM
 */

package org.rummage.slide;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 *
 * @author nelsnelson
 */
public class JTextPane
    extends javax.swing.JTextPane
{
    /** Creates a new instance of JTextPane */
    public JTextPane() {
        super();
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
