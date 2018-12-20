/*
 * JContextPopupMenu.java
 *
 * Created on February 3, 2006, 1:08 PM
 */
package org.rummage.slide;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

/**
 *
 * @author not attributable
 */
public abstract class JContextPopupMenu
    extends JPopupMenu
    implements MouseListener
{
    protected JComponent invoker = null;
    
    /**
     * Creates a new instance of JContextPopupMenu
     */
    public JContextPopupMenu() {
        this(null);
    }
    
    /**
     * Creates a new instance of JContextPopupMenu
     */
    public JContextPopupMenu(JComponent invoker) {
        super();
        
        setInvoker(invoker);
    }
    
    protected void setInvoker(JComponent invoker) {
        if (invoker == null) {
            return;
        }
        
        this.invoker = invoker;
        
        super.setInvoker(invoker);
        
        invoker.add(this);
        invoker.addMouseListener(this);
        
        addNotify();
    }
    
    public JComponent getInvoker() {
        return invoker;
    }
    
    protected abstract void update();

    public void mouseClicked(MouseEvent e) {}

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent e) {
        Object o = e.getSource();
        
        if (o instanceof JComponent) {
            invoker = (JComponent) o;
        }
        
        if (invoker == null) {
            return;
        }
        
        invoker.requestFocus();
        
        if (e.isPopupTrigger()) {
            update();
            show(invoker, e.getX(), e.getY());
        }
    }

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent e) {
        Object o = e.getSource();
        
        if (o instanceof JComponent) {
            invoker = (JComponent) o;
        }
        
        if (invoker == null) {
            return;
        }
        
        invoker.requestFocus();
        
        if (e.isPopupTrigger()) {
            update();
            show(invoker, e.getX(), e.getY());
        }
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}
}
