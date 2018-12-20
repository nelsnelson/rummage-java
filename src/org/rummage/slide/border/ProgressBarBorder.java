/*
 * ProgressBarBorder.java
 *
 * Created on April 24, 2006, 8:29 AM
 */

package org.rummage.slide.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.border.AbstractBorder;

/**
 *
 * @author nelsnelson
 */
public class ProgressBarBorder
    extends AbstractBorder
{
    /**
     * Paints the border for the specified component with the specified 
     * position and size.
     * @param c the component for which this border is being painted
     * @param g the paint graphics
     * @param x the x position of the painted border
     * @param y the y position of the painted border
     * @param width the width of the painted border
     * @param height the height of the painted border
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width,
        int height)
    {
        Graphics2D g2 = (Graphics2D) g.create();
        
        int w = width - 1;
        int h = height - 1;
        
        g2.translate(x, y);
        g2.setColor(new Color(172, 171, 167));
        g2.drawLine(0, 1, 1, 0); // top left corner
        g2.drawLine(0, h - 1, 1, h); // bottom left corner
        g2.drawLine(w - 1, h , w, h - 1); // bottom right corner
        
        g2.setColor(new Color(179, 179, 179));
        g2.drawLine(w, 1, w, 1); // top right corner
        
        g2.setColor(new Color(129, 129, 129));
        g2.drawLine(w, 2, w, 2); // top right corner
        
        g2.setColor(new Color(104, 104, 104));
        g2.drawLine(w - 1, 0, w - 1, 1); // top right corner
        
        g2.setColor(new Color(127, 126, 125));
        g2.drawLine(0, 2, 2, 0); // top left corner
        g2.drawLine(0, h - 2, 2, h); // bottom left corner
        g2.drawLine(w, 3, w, 4); // top right corner
        g2.drawLine(w - 2, h , w , h - 2); // bottom right corner
        
        g2.setColor(new Color(190, 190, 190));
        g2.drawLine(2, 2, 2, 2); // top left corner
        g2.drawLine(w - 2, 1, w - 1, 2); // top right corner
        
        g2.setColor(new Color(239, 239, 239));
        g2.drawLine(3, 3, 3, 3); // top left corner
        g2.drawLine(3, h - 2, 3, h - 2); // bottom left corner
        g2.drawLine(w - 2, 3, w, 3); // top right corner
        g2.drawLine(w - 2, h - 2, w -2, h - 2); // bottom right corner
        
        g2.setColor(new Color(104, 104, 104));
        g2.drawLine(3, 0, w - 1, 0); // top
        g2.drawLine(3, h, w - 3, h); // bottom
        g2.drawLine(0, 3, 0, h - 3); // left
        g2.drawLine(w, 3, w, h - 3); // right
        
        g2.setColor(new Color(190, 190, 190));
        g2.drawLine(2, 1, w - 2, 1); // top
        g2.drawLine(1, 2, 1, h - 2); // left
        
        g2.setColor(new Color(239, 239, 239));
        g2.drawLine(3, 2, w - 2, 2); // top
        g2.drawLine(3, h - 1, w - 2, h - 1); // bottom
        g2.drawLine(2, 3, 2, h - 1); // left
        g2.drawLine(w - 1, 3, w - 1, h - 2); // right
        
        g2.dispose();
    }
}
