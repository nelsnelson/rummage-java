/*
 * DragAndDropable.java
 *
 * Created on December 13, 2005, 3:06 PM
 */

package org.rummage.slide.dnd;

import java.awt.Component;
import java.awt.Point;

/**
 *
 * @author nelsnelson
 */
public interface DragAndDropable {
    public void setDragAndDropPartner(Component partner);
    
    public void setDraggingOver(Point p);
    
    public void dropped(Object data, Point p);
}
