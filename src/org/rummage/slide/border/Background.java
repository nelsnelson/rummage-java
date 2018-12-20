/*
 * Background.java
 *
 * Created on June 29, 2006, 3:08 PM
 */

package org.rummage.slide.border;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.border.Border;
import javax.swing.event.AncestorListener;

import org.rummage.slide.AncestorAdapter;
import org.rummage.slide.ColorUtilities;
import org.rummage.slide.SlideUtilities;

/**
 *
 * @author nnelson
 */
public class Background
    implements Border
{
    private ImageIcon image = null;
    private JComponent c = null;
    private JComponent c1 = null;
    private JViewport viewport = null;
    
    public static final int PLAIN = 1;
    public static final int WATERMARK = 2;
    
    private boolean repeatingBackground = false;
    private int opacityMode = WATERMARK;
    
    public Background(JComponent c) {
        this.c = c;
        
        applyBackground(c);
    }
    
    public void setOpacity(int mode) {
        opacityMode = mode;
    }
    
    public void setBackgroundImage(ImageIcon image) {
        this.image = image;
    }
    
    public void setRepeatingBackground(boolean b) {
        repeatingBackground = b;
    }
    
    public JComponent getComponent() {
        return c;
    }
    
    public Background getBackground() {
        return this;
    }
    
    public void repaint() {
        paintBorder(c, c.getGraphics(), 0, 0, c.getWidth(), c.getHeight());
        
        c.paint(c.getGraphics());
    }
    
    public void paintBorder(Component c, Graphics g, int x, int y, int width,
        int height)
    {
        Graphics2D g2 = (Graphics2D) g.create();
        
        // First, paint a fake background for the component, since it is not
        // opaque anymore, and will display the default ugly gray of its
        // parent component instead of its own background color unless it is
        // covered up.
        Paint oldPaint = g2.getPaint();
        
        Color background = getComponent().getBackground();
        
        // ensure that the color is opaque
        background =
            ColorUtilities.getTranslucent(background, 255);
        
        // fake background
        g2.setPaint(background);
        g2.fill(new Rectangle2D.Double(x, y, width, height));
        g2.setPaint(oldPaint);
        
        if (image == null) {
            getComponent().setOpaque(true);
            
            return;
        }
        
        // ensure that the image is contained within the applicable component
        g2.setClip(x, y, width, height);
        
        // The component may not be opaque, otherwise, it will cover up the
        // special image.
        getComponent().setOpaque(false);
        
        // Paint the image on top of the background, but still behind the
        // component.
        if (repeatingBackground) {
            for (int x0 = x; x0 < width + x; x0 += image.getIconWidth()) {
                for (int y0 = y; y0 < height + y; y0 += image.getIconHeight()) {
                    drawBackground(g2, image.getImage(), x0, y0);
                }
            }
        }
        else {
            int x0 = x + Math.max(0, width - image.getIconWidth()) / 2;
            int y0 = y + Math.max(0, height - image.getIconHeight()) / 2;
            
            drawBackground(g2, image.getImage(), x0, y0);
        }
    }
    
    private void drawBackground(Graphics g, Image image, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        
        if (opacityMode == WATERMARK) {
            // make next drawing transparent
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));     
            g2.drawImage(image, x, y, null, null);

            // turn off alpha for flat image
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.00f));
        }
        else {
            g2.drawImage(image, x, y, null, null);
        }
    }
 
    public Insets getBorderInsets(Component c) {
        return new Insets(0,0,0,0);
    }
 
    public boolean isBorderOpaque() {
        return true;
    }

    private void applyBackground(final JComponent c) {
        c.setOpaque(false);
        
        c.addAncestorListener(new AncestorAdapter(c) {
            public void parentChanged() {
                Container container = getParent();
                
                if (container instanceof JViewport) {
                    JViewport viewport = (JViewport) container;
                    
                    Container parent = viewport.getParent();
                    
                    JScrollPane scrollPane = (JScrollPane) parent;
                    
                    applyJScrollPaneHack(scrollPane);
                }
                else if (container instanceof JComponent) {
                    JComponent c1 = (JComponent) container;
                    
                    applyJComponentHack(c1);
                }
                
                // This only needs to be done once, so stop listening for
                // parent changes.
                if (SlideUtilities.isListening(c, AncestorListener.class,
                    this))
                {
                    c.removeAncestorListener(this);
                }
            }
        });
    }
    
    private void applyJScrollPaneHack(final JScrollPane sp) {
        viewport = sp.getViewport();
        
        viewport.setOpaque(false);
        sp.setViewportBorder(BorderFactory.createCompoundBorder(getBackground(),
            sp.getViewportBorder()));
        sp.repaint();
    }
    
    private void applyJComponentHack(final JComponent c1) {
        this.c1 = c1;
        
        c1.setOpaque(false);
        c1.setBorder(BorderFactory.createCompoundBorder(getBackground(),
            c1.getBorder()));
        c1.repaint();
    }
}