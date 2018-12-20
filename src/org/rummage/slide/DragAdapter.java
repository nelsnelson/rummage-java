/*
 * DragAdapter.java
 *
 * Created on April 7, 2006, 2:22 PM
 */

package org.rummage.slide;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;

import org.rummage.slide.event.ComponentSelectionEvent;

/**
 *
 * @author nelsnelson
 */
public class DragAdapter
    implements MouseListener, MouseMotionListener
{
    public static final int NO_BORDER = 1;
    public static final int PAINTED_BORDER = 2;
    
    public static final int WIDGET = 1;
    public static final int DRAGS_PARENT = 2;
    
    private JComponent c = null;
    private Component parent = null;
    private Point p0 = null;
    
    private static boolean parentDragger = true;
    private static int grabbedBorderWidth = 5;
    private static double translucenyPercentage = 0.50d;
    private static Color selectionColor = ColorUtilities.getTranslucent(Color.BLUE, translucenyPercentage);
    private static Rectangle releasedBounds = null;
    private static Rectangle grabbedBounds = null;
    private static boolean borderPainted = true;
    private static Border releasedBorder = null;
    private static Border grabbedBorder =
        BorderFactory.createLineBorder(selectionColor, grabbedBorderWidth);
    
    private List<ComponentSelectionListener> componentSelectionListeners =
        new ArrayList();
    
    /** Creates a new instance of DragAdapter */
    public DragAdapter(JComponent c) {
        this(c, PAINTED_BORDER, WIDGET);
    }
    
    /** Creates a new instance of DragAdapter */
    public DragAdapter(JComponent c, int borderStyle) {
        this(c, PAINTED_BORDER, WIDGET);
    }
    
    /** Creates a new instance of DragAdapter */
    public DragAdapter(JComponent c, int borderStyle, int role) {
        this.c = c;
        
        setBorderPainted(borderStyle == PAINTED_BORDER);
        setParentDragger(role == DRAGS_PARENT);
        
        c.addMouseListener(this);
        c.addMouseMotionListener(this);
        c.addAncestorListener(new AncestorAdapter(c) {
            public void parentChanged() {
                if (parent == null) {
                    parent = getWindow();
                }
            }
        });
        
        DragManager.add(c, this);
    }

    public void setBorderPainted(boolean b) {
        borderPainted = b;
    }

    public void setParentDragger(boolean b) {
        parentDragger = b;
    }
    
    public void stopDragging() {
        stopDragging(c);
    }
    
    public static void stopDragging(JComponent c) {
        DragAdapter adapter = DragManager.get(c);
        
        c.removeMouseListener(adapter);
        c.removeMouseMotionListener(adapter);
        c.setOpaque(true);
    }
    
    public void resumeDragging() {
        resumeDragging(c);
    }

    public static void resumeDragging(JComponent c) {
        DragAdapter adapter = DragManager.get(c);
        
        c.addMouseListener(adapter);
        c.addMouseMotionListener(adapter);
    }
    
    public void setSelectedBorderWidth(int width) {
        grabbedBorderWidth = width;
        
        grabbedBorder =
            BorderFactory.createLineBorder(selectionColor, grabbedBorderWidth);
    }
    
    public void setSelectionColor(Color color) {
        selectionColor = ColorUtilities.getTranslucent(color, translucenyPercentage);
    }
    
    public void setSelected(JComponent c, boolean b) {
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
        fireComponentSelectedEvent(c);
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
        if (borderPainted) setSelected(c, true);
                
        p0 = e.getPoint();
        Container parent = c.getParent();
        parent.add(c, 0);
        
        c.grabFocus();
    }
    
    public void mouseReleased(MouseEvent e) {
        if (borderPainted) setSelected(c, false);
        
        e.consume();
    }
    
    public void mouseEntered(MouseEvent e) {
        e.consume();
    }
    
    public void mouseExited(MouseEvent e) {
        e.consume();
    }
    
    public void mouseDragged(MouseEvent e) {
        Point loc =
            parentDragger && parent != null ?
                parent.getLocation() : c.getLocation();
        
        Point p1 = new Point();
        p1.x = e.getX() + loc.x - p0.x - (borderPainted ? grabbedBorderWidth : 0);
        p1.y = e.getY() + loc.y - p0.y - (borderPainted ? grabbedBorderWidth : 0);
        
        if (parentDragger && parent != null) {
            parent.setLocation(p1);
        }
        else {
            c.setLocation(p1);
        }
    }
    
    public void mouseMoved(MouseEvent e) {
        e.consume();
    }
    
    public void addComponentSelectionListener(ComponentSelectionListener l) {
        if (!componentSelectionListeners.contains(l)) {
            componentSelectionListeners.add(l);
        }
    }
    
    public List<ComponentSelectionListener> getComponentSelectionListeners() {
        return componentSelectionListeners;
    }
    
    private void fireComponentSelectedEvent(JComponent c) {
        List<ComponentSelectionListener> componentSelectionListeners =
            getComponentSelectionListeners();
        
        for (ComponentSelectionListener l : componentSelectionListeners) {
            l.componentSelected(new ComponentSelectionEvent(c));
        }
    }
    
    public static DragAdapter getDragAdapterForComponent(JComponent c) {
        if (c == null) {
            return null;
        }
        
        MouseListener[] listeners = c.getMouseListeners();
        
        for (MouseListener l : listeners) {
            if (l instanceof DragAdapter) {
                DragAdapter da = (DragAdapter) l;
                
                if (da.c != null && da.c.equals(c)) {
                    return da;
                }
            }
        }
        
        return null;
    }
    
    private static class DragManager {
        private static java.util.Map<JComponent, DragAdapter> adapters = null;
        
        public static void add(JComponent c, DragAdapter adapter) {
            getAdapters().put(c, adapter);
        }
        
        public static DragAdapter remove(JComponent c) {
            DragAdapter adapter = getAdapters().remove(c);
            
            return adapter;
        }
        
        private static java.util.Map<JComponent, DragAdapter> getAdapters() {
            if (adapters == null) {
                adapters = createAdaptersMap();
            }
            
            return adapters;
        }
        
        private static java.util.Map<JComponent,
            DragAdapter> createAdaptersMap()
        {
            adapters = new java.util.WeakHashMap();
            
            return adapters;
        }

        private static DragAdapter get(JComponent c) {
            return getAdapters().get(c);
        }
    }
}
