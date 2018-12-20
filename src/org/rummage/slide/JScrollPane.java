/*
 * JScrollPane.java
 *
 * Created on February 22, 2006, 8:31 AM
 */

package org.rummage.slide;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JScrollBar;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author nelsnelson
 */
public class JScrollPane 
    extends javax.swing.JScrollPane
    implements ChangeListener, CornerPeer
{
    private JScrollBar vsb = new JScrollBar(JScrollBar.VERTICAL) {
        public void setEnabled(boolean x)  {
            super.setEnabled(x);
            
            if (org.rummage.toolbox.util.UIManager.
                getLookAndFeel().getName().contains("Windows"))
            {
                Component[] children = getComponents();
                for(int i = 0; i < children.length; i++) {
                    children[i].setEnabled(x);
                }
            }
        }
    };
    
    private JScrollBar hsb = new JScrollBar(JScrollBar.HORIZONTAL) {
        public void setEnabled(boolean x)  {
            super.setEnabled(x);
            
            if (org.rummage.toolbox.util.UIManager.
                getLookAndFeel().getName().contains("Windows"))
            {
                Component[] children = getComponents();
                for(int i = 0; i < children.length; i++) {
                    children[i].setEnabled(x);
                }
            }
        }
    };
    
    /** Creates a new instance of JScrollPane */
    public JScrollPane() {
        this(null, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_AS_NEEDED, false);
    }
    
    public JScrollPane(Component view) {
        this(view, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_AS_NEEDED, false);
    }
    
    public JScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
        
        vsb.getModel().addChangeListener(this);
        hsb.getModel().addChangeListener(this);
        
        setVerticalScrollBar(vsb);
        setHorizontalScrollBar(hsb);
        
        setAutoscrolls(true);
        
        vsb.setUnitIncrement(16);
    }
    
    private Border defaultBorder = null;
    
    public JScrollPane(Component view, int vsbPolicy, int hsbPolicy,
        boolean showBorder)
    {
        this(view, vsbPolicy, hsbPolicy);
        
        if (showBorder) {
            if (defaultBorder != null) setBorder(defaultBorder);
        }
        else {
            defaultBorder = getBorder();
            setBorder(BorderFactory.createEmptyBorder());
        }
    }
    
    public void stateChanged(ChangeEvent e) {
        vsb.setEnabled(vsb.getVisibleAmount() <
            getViewport().getViewSize().getHeight());
        
        hsb.setEnabled(hsb.getVisibleAmount() <
            getViewport().getViewSize().getWidth());
    }
    
    public void setCornerComponent(Component corner) {
        setCorner(JScrollPane.LOWER_RIGHT_CORNER, corner);
    }
}
