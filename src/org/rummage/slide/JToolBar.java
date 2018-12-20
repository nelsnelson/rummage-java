/*
 * JToolBar.java
 *
 * Created on February 8, 2006, 9:06 AM
 */
package org.rummage.slide;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.border.Border;

import org.rummage.slide.border.SideBorder;
import org.rummage.toolbox.util.UIManager;

/**
 *
 * @author nelsnelson
 */
public class JToolBar 
    extends javax.swing.JToolBar
{
    private JMenuBar menuBar = null;
    
    /** Creates a new instance of JToolBar */
    public JToolBar() {
        this(null, JToolBar.HORIZONTAL, null);
    }
    
    /** Creates a new instance of JToolBar */
    public JToolBar(String name) {
        this(name, JToolBar.HORIZONTAL, null);
    }
    
    /** Creates a new instance of JToolBar */
    public JToolBar(int orientation) {
        this(null, orientation, null);
    }
    
    /** Creates a new instance of JToolBar */
    public JToolBar(JMenuBar menuBar) {
        this(null, JToolBar.HORIZONTAL, menuBar);
    }
    
    /** Creates a new instance of JToolBar */
    public JToolBar(String name, JMenuBar menuBar) {
        this(name, JToolBar.HORIZONTAL, menuBar);
    }
    
    /** Creates a new instance of JToolBar */
    public JToolBar(int orientation, JMenuBar menuBar) {
        this(null, orientation, menuBar);
    }
    
    /** Creates a new instance of JToolBar */
    public JToolBar(String name, int orientation, JMenuBar menuBar) {
        super(name, orientation);
        
        this.menuBar = menuBar;
        
        setBorder(createToolBarBorder());
        
        setFloatable(false);
    }
    
    public Component add(String text) {
        return super.add(new JToolBarButton(text));
    }
    
    public Component add(JButton button) {
        button.setFocusable(false);
        
        return super.add(button);
    }
    
    public void setActionListener(ActionListener listener) {
        Component[] components = getComponents();
        
        for (Component c : components) {
            if (c instanceof JButton) {
                JButton button = (JButton) c;
                
                button.addActionListener(listener);
            }
        }
    }

    private Border createToolBarBorder() {
        Border b1 = new SideBorder(SideBorder.SOUTH, Color.LIGHT_GRAY);
        Border b2 = new SideBorder(SideBorder.SOUTH, Color.GRAY);
        
        Border b3 = BorderFactory.createCompoundBorder(b2, b1);
        
        return b3;
    }
    
    public void setPeer(JMenuBar menuBar) {
        this.menuBar = menuBar;
    }
    
    public void setVisible(boolean b) {
        if (menuBar != null) {
            Border border = null;
            
            if (b) {
                border =
                    BorderFactory.createCompoundBorder(new SideBorder(SideBorder.SOUTH, Color.WHITE),
                    new SideBorder(SideBorder.SOUTH, Color.LIGHT_GRAY));
            }
            else {
                border = (Border) UIManager.get("MenuBar.border");
            }
            
            menuBar.setBorder(border);
            menuBar.setBorderPainted(true);
        }
        
        super.setVisible(b);
    }
    
    class JToolBarButton
        extends javax.swing.JButton
    {
        public JToolBarButton(String text) {
            super(text);
        }
        
        public boolean isFocusable() { return false; }
        public void requestFocus() {}
    }
}
