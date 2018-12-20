/*
 * DragAdapter.java
 *
 * Created on April 7, 2006, 2:22 PM
 */

package org.rummage.slide.dnd;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;

import org.rummage.slide.ColorUtilities;

/**
 *
 * @author nelsnelson
 */
public class DragAdapter
    implements MouseListener, MouseMotionListener
{
    private JComponent c = null;
    private Point p0 = null;
    
    private static int grabbedBorderWidth = 4;
    private static double translucenyPercentage = 0.50d;
    private static Color selectionColor = ColorUtilities.getTranslucent(Color.BLUE, translucenyPercentage);
    private static Rectangle releasedBounds = null;
    private static Rectangle grabbedBounds = null;
    private static Border releasedBorder = null;
    private static Border grabbedBorder =
        BorderFactory.createLineBorder(selectionColor, grabbedBorderWidth);
    
    /** Creates a new instance of DragAdapter */
    public DragAdapter(JComponent c) {
        this.c = c;
        
        c.addMouseListener(this);
        c.addMouseMotionListener(this);
        
        DragManager.add(c, this);
    }
    
    public static void stopDragging(JComponent c) {
        DragAdapter adapter = DragManager.remove(c);
        
        c.removeMouseListener(adapter);
        c.removeMouseMotionListener(adapter);
        c.setOpaque(true);
    }
    
    public void setSelectedBorderWidth(int width) {
        grabbedBorderWidth = width;
    }
    
    public void setSelectionColor(Color color) {
        selectionColor = ColorUtilities.getTranslucent(color, translucenyPercentage);
    }
    
    public static void setSelected(JComponent c, boolean b) {
        if (b) {
            releasedBorder = c.getBorder();
            grabbedBounds = c.getBounds();
            c.setBorder(BorderFactory.createCompoundBorder(grabbedBorder,
                releasedBorder));
            c.setBounds(createGrabbedBounds(c));
        }
        else {
            c.setBorder(releasedBorder);
            c.setBounds(createReleasedBounds(c));
        }
    }
    
    public static Rectangle createGrabbedBounds(JComponent c) {
        grabbedBounds =
            new Rectangle(c.getX() - (grabbedBorderWidth),
                c.getY() - (grabbedBorderWidth),
                c.getWidth() + (grabbedBorderWidth * 2),
                c.getHeight() + (grabbedBorderWidth * 2));
        
        return grabbedBounds;
    }
    
    public static Rectangle createReleasedBounds(JComponent c) {
        releasedBounds = c.getBounds();
        
        releasedBounds.x = c.getX() + (grabbedBorderWidth);
        releasedBounds.y = c.getY() + (grabbedBorderWidth);
        releasedBounds.width = c.getWidth() - (grabbedBorderWidth * 2);
        releasedBounds.height = c.getHeight() - (grabbedBorderWidth * 2);
        
        return releasedBounds;
    }
    
    public void mouseClicked(MouseEvent e) {
        e.consume();
    }
    
    public void mousePressed(MouseEvent e) {
        setSelected(c, true);
                
        p0 = e.getPoint();
        Container parent = c.getParent();
        parent.add(c, 0);
    }
    
    public void mouseReleased(MouseEvent e) {
        setSelected(c, false);
        
        e.consume();
    }
    
    public void mouseEntered(MouseEvent e) {
        //java.awt.Window window = ContextManager.getFocusedWindow();
        //window.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.
        //    Cursor.MOVE_CURSOR));
        //
        e.consume();
    }
    
    public void mouseExited(MouseEvent e) {
        //java.awt.Window window = ContextManager.getFocusedWindow();
        //window.setCursor(null);
        e.consume();
    }
    
    public void mouseDragged(MouseEvent e) {
        Point loc = c.getLocation();
        Point p1 = new Point();
        p1.x = e.getX() + loc.x - p0.x - (grabbedBorderWidth);
        p1.y = e.getY() + loc.y - p0.y - (grabbedBorderWidth);
        
        c.setLocation(p1);
    }
    
    public void mouseMoved(MouseEvent e) {
        e.consume();
    }
    
    static class DragManager {
        private static java.util.Map adapters = null;
        
        public static void add(JComponent c, DragAdapter adapter) {
            getAdapters().put(c, adapter);
        }
        
        public static DragAdapter remove(JComponent c) {
            DragAdapter adapter = (DragAdapter) getAdapters().remove(c);
            
            return adapter;
        }
        
        private static java.util.Map getAdapters() {
            if (adapters == null) {
                adapters = createAdaptersMap();
            }
            
            return adapters;
        }
        
        private static java.util.Map createAdaptersMap() {
            adapters = new java.util.HashMap();
            
            return adapters;
        }
    }
}
