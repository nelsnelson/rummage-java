/*
 * ComponentSelectionEvent.java
 *
 * Created on June 27, 2006, 4:27 PM
 */

package org.rummage.slide.event;

import java.awt.Component;
import java.util.EventObject;
import java.util.Vector;

/**
 *
 * @author nelsnelson
 */
public class ComponentSelectionEvent
    extends EventObject
{
    private Component c = null;
    private Vector<Component> recentlySelectedComponents = null;
    
    /** Creates a new instance of ComponentSelectionEvent */
    public ComponentSelectionEvent(Object source) {
        super(source);
        
        if (!(source instanceof java.awt.Component)) {
            throw new IllegalArgumentException("The source parameter of a " +
                "ComponentSelectionEvent must be a java.awt.Component.");
        }
        
        this.c = (Component) source;
        
        Vector<Component> recentlySelectedComponents =
            getMostRecentlySelectedComponents();
        
        if (recentlySelectedComponents.contains(c)) {
            recentlySelectedComponents.remove(c);
        }
        
        recentlySelectedComponents.add(c);
    }
    
    public Component getMostRecentlySelectedComponent() {
        return recentlySelectedComponents.lastElement();
    }

    public Vector<Component> getMostRecentlySelectedComponents() {
        if (recentlySelectedComponents == null) {
            recentlySelectedComponents = new Vector();
        }
        
        return recentlySelectedComponents;
    }
}
