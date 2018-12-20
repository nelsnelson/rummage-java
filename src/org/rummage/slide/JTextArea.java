/*
 * JTextArea.java
 *
 * Created on January 18, 2006, 11:17 AM
 */

package org.rummage.slide;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.text.Document;

/**
 *
 * @author nelsnelson
 */
public class JTextArea
    extends javax.swing.JTextArea
{
    /** Creates a new instance of JTextArea */
    public JTextArea() {
        this((String) null);
    }
    
    public JTextArea(String text) {
        super(text);
    }
    
    public JTextArea(Document doc) {
        super(doc);
    }
    
    public JTextArea(Document doc, String text, int rows, int columns) {
        super(doc, text, rows, columns);
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

            super.paint(g2);

            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
        else {
            super.paint(g);
        }
    }
    
    public void reset() {
        clear();
    }
    
    public void clear() {
        setText("");
    }
}
