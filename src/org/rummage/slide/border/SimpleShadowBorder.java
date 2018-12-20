/*
 * SimpleShadowBorder.java
 *
 * Created on July 28, 2005, 1:39 PM
 */
package org.rummage.slide.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.border.BevelBorder;

/**
 *
 * @author nelsnelson
 */
public class SimpleShadowBorder
    extends BevelBorder {
    
    /** Creates a new instance of SimpleShadowBorder */
    public SimpleShadowBorder() {
        this(BevelBorder.LOWERED);
    }
    
    /** Creates a new instance of SimpleShadowBorder */
    public SimpleShadowBorder(int style) {
        super(style);
    }

    protected void paintLoweredBevel(Component c, Graphics g, int x, int y,
                                        int width, int height)  {
        Color oldColor = g.getColor();
        int h = height;
        int w = width;

        g.translate(x, y);

        g.setColor(getShadowInnerColor(c));
        g.drawLine(0, 0, 0, h-2);
        g.drawLine(1, 0, w-2, 0);

        g.setColor(getHighlightOuterColor(c));
        g.drawLine(0, h-1, w-1, h-1);
        g.drawLine(w-1, 0, w-1, h-2);

        g.translate(-x, -y);
        g.setColor(oldColor);
    }
}
