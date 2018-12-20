/*
 * FieldGroupAdapter.java
 *
 * Created on November 14, 2005, 11:09 AM
 */

package org.rummage.slide;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JComponent;


/**
 *
 * @author not attributable
 */
public class FieldGroupAdapter
    implements MouseListener
{
    protected ArrayList<JComponent> group;
    
    /**
     * Creates a new instance of FieldGroupAdapter 
     */
    public FieldGroupAdapter() {
        group = new ArrayList();
    }
    
    /** Creates a new instance of SingleFocusableComponentAdapter */
    public FieldGroupAdapter(JComponent component) {
        this();
        
        component.addMouseListener(this);
        group.add(component);
    }
    
    /**
     * Creates a new instance of FieldGroupAdapter 
     */
    public FieldGroupAdapter(JComponent... components) {
        this();
        
        for (int i = 0, n = components.length; i < n; i++) {
            components[i].addMouseListener(this);
            group.add(components[i]);
        }
    }
    
    public void add(JComponent component) {
        component.addMouseListener(this);
        group.add(component);
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     */
    public void mouseClicked(MouseEvent e) {}

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent e) {
        Iterator i = group.iterator();
        
        while (i.hasNext()) {
            JTable selectable = (JTable) i.next();
            
            if (selectable.equals(e.getSource())) {
                continue;
            }
            
            selectable.getSelectionModel().clearSelection();
        }
    }

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent e) {}

    /**
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent e) {}

    /**
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent e) {}
}
