/*
 * JTextField.java
 *
 * Created on February 28, 2006, 3:39 PM
 */

package org.rummage.slide;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

/**
 *
 * @author nelsnelson
 */
public class JTextField
    extends javax.swing.JTextField
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Creates a new instance of JTextField */
    public JTextField() {
        super();
        
        setMargin(new Insets(2, 2, 1, 2));
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
