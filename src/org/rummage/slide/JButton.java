/*
 * JButton.java
 *
 * Created on June 1, 2006, 3:11 PM
 */

package org.rummage.slide;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Action;
import javax.swing.Icon;

/**
 *
 * @author unnattributable
 */
public class JButton
    extends javax.swing.JButton
{
    public JButton() {
        super();
        
        setOpaque(false);
    }

    public JButton(Action a) {
        super(a);
        
        setOpaque(false);
    }

    public JButton(Icon icon) {
        super(icon);
        
        setOpaque(false);
    }

    public JButton(String text) {
        super(text);
        
        setOpaque(false);
    }

    public JButton(String text, Icon icon) {
        super(text, icon);
        
        setOpaque(false);
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
    
    //private boolean focusable = true;
    
    //public boolean isFocusable() { return focusable; }
    //public void requestFocus() { if (focusable) super.requestFocus(); };
    
    //public void setFocusable(boolean b) {
    //    focusable = b;
    //}

    public void setResponsive(boolean b) {
        if (!b) {
            setModel(new javax.swing.DefaultButtonModel() {
                public boolean isPressed() {
                    return false;
                }

                public void setPressed(boolean b) {
                    // do nothing
                }

                public boolean isRollover() {
                    return false;
                }

                public void setRollover(boolean b) {
                    // do nothing
                }
            });
        }
        else {
            setModel(new javax.swing.DefaultButtonModel());
        }
    }
}
