/*
 * SideBorder.java
 *
 * Created on July 28, 2005, 1:39 PM
 */

package org.rummage.slide.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.border.EmptyBorder;

/**
 *
 * @author nelsnelson
 */
public class SideBorder
    extends EmptyBorder
{
    protected String side;
    protected Color color = null;
    
    public static final String NORTH = java.awt.BorderLayout.NORTH;
    public static final String WEST = java.awt.BorderLayout.WEST;
    public static final String SOUTH = java.awt.BorderLayout.SOUTH;
    public static final String EAST = java.awt.BorderLayout.EAST;
    
    /** Creates a new instance of SideBorder */
    public SideBorder(String side) {
        super(side.equals(NORTH) ? 1 : 0, side.equals(WEST) ? 1 : 0,
              side.equals(SOUTH) ? 1 : 0, side.equals(EAST) ? 1 : 0);
        
        this.side = side;
    }
    
    /** Creates a new instance of SideBorder */
    public SideBorder(String side, Color color) {
        this(side);
        
        this.color = color;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width,
        int height)
    {
        Color oldColor = g.getColor();
        int h = height;
        int w = width;

        g.translate(x, y);
        
        if (color == null) {
            color = c.getBackground().darker();
        }
        
        g.setColor(color);
        
        if (side.equals(NORTH)) {
            g.drawLine(0, 0, w-1, 0);
        }
        else if (side.equals(EAST)) {
            g.drawLine(w-1, 0, w-1, h-1);
        }
        else if (side.equals(WEST)) {
            g.drawLine(0, 0, 0, h-1);
        }
        else if (side.equals(SOUTH)) {
            g.drawLine(0, h-1, w-1, h-1);
        }

        g.translate(-x, -y);
        g.setColor(oldColor);
    }
}
