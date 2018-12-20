/*
 * AncestorAdapter.java
 *
 * Created on June 29, 2006, 2:30 PM
 */

package org.rummage.slide;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;

import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author nnelson
 */
public class AncestorAdapter
    implements AncestorListener
{
    private Container parent = null;
    private Component child = null;
    
    /** Creates a new instance of AncestorAdapter */
    public AncestorAdapter(Component child) {
        this.child = child;
    }
    
    public void parentChanged() {
        // stub - override
    }
    
    public Window getWindow() {
        JRootPane rootPane = SwingUtilities.getRootPane(child);
        
        return rootPane == null ? null : (Window) rootPane.getParent();
    }
    
    public Container getParent() {
        return parent;
    }
    
    public void setParent(Container parent) {
        this.parent = parent;
        
        parentChanged();
    }
    
    public void ancestorAdded(AncestorEvent e) {
        setParent(e.getComponent().getParent());
    }

    public void ancestorMoved(AncestorEvent e) {
        
    }

    public void ancestorRemoved(AncestorEvent e) {
        
    }
}
