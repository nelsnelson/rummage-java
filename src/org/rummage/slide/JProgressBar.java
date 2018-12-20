/*
 * JProgressBar.java
 *
 * Created on April 7, 2006, 9:59 AM
 */

package org.rummage.slide;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.rummage.slide.border.ProgressBarBorder;
import org.rummage.toolbox.util.Calculus;

/**
 *
 * @author nnelson
 */
public class JProgressBar
    extends javax.swing.JComponent
    implements ActionListener
{
    private javax.swing.Timer t = null;
    private int x = 0;
    private int w = 10;
    private double increment = 1.000d;
    private BufferedImage image = null;
    private Color fg = new Color(67, 216, 69);
    private Color ffg = null;
    
    public JProgressBar() {
        setBackground(Color.WHITE);
        setForeground(fg);
        setBorder(new ProgressBarBorder());
        setTimer(new javax.swing.Timer(100, this));
    }
    
    public void start() {
        getTimer().start();
    }
    
    public void stop() {
        getTimer().stop();
    }
    
    public void toggle() {
        if (getTimer().isRunning()) {
            stop();
        }
        else {
            start();
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        increment();
        repaint();
    }
    
    private void increment() {
        x = x + 1;
        x = x % (w+1);
    }
    
    private void setTimer(javax.swing.Timer t) {
        this.t = t;
    }
    
    public javax.swing.Timer getTimer() {
        return t;
    }
    
    public void setSpeed(int speed) {
        if (speed > 100) {
            increment = 0.001d;
        }
        else {
            increment = 1.000d;
        }
        
        getTimer().setDelay(Calculus.constrain(speed, 1, 100));
    }
    
    public void setBoxWidth(int width) {
        w = Calculus.constrain(width, 0, getWidth()/2);
    }
    
    public void setForeground(Color fg) {
        super.setForeground(fg);
        setFadedForeground(fg);
    }
    
    public void setFadedForeground(Color fg) {
        ffg = ColorUtilities.lighten(fg, 0.50d);
    }
    
    public Color getFadedForeground() {
        return ffg;
    }
    
    public void setDraggable(boolean b) {
        if (b) {
            new DragAdapter(this);
        }
    }
    
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        g2.translate(getInsets().left, getInsets().top);
        
        int width = getWidth();
        int height = getHeight();
        
        if (image == null) {
            image = (BufferedImage)(this.createImage(width, height));
        }
        
        drawProgressBar(image.createGraphics());
        
        g2.drawImage(image, null, 0, 0);
        
        g2.dispose();
    }
    
    private void drawProgressBar(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        
        Graphics2D g2 = (Graphics2D) g.create();
        
        g2.setColor(getBackground());
        g2.fill(new Rectangle2D.Double(0, 0, width, height));
        
        // only paint the glow if the progress bar is running
        if (getTimer().isRunning()) {
            drawGlow(g2);
        }
        
        g2.dispose();
    }
    
    private void drawGlow(Graphics g) {
        int width = getWidth() - 2;
        int height = getHeight() - 2;
        
        Graphics2D g2 = (Graphics2D) g.create();
        
        g2.translate(-4, -3);
        
        GradientPaint paint1 =
            new GradientPaint(0, 0, new Color(251, 251, 251), 0,
            (height - 3)/2, new Color(240, 240, 240), true);
        
        g2.translate(4, 3);
        g2.setPaint(paint1);
        g2.fill(new Rectangle2D.Double(0, 2, 0, height - 2));
        
        GradientPaint paint2 =
            new GradientPaint(0, 0, getFadedForeground(), 0,
            (height - (2 + getInsets().top + getInsets().bottom))/2,
            getForeground(), true);
        
        g2.translate(4, 3);
        g2.setPaint(paint2);
        g2.fill(new Rectangle2D.Double(0, 0,
            width - (5 + getInsets().left + getInsets().right),
            height - (4 + getInsets().top + getInsets().bottom)));
        
        g2.translate(x, 0);
        drawPartitions(g2);
    }

    private void drawPartitions(Graphics g) {
        int width = getWidth() - 4;
        int height = getHeight() - 2;
        
        Graphics2D g2 = (Graphics2D) g.create();
        
        if (increment < 1) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY);
        
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                            RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        }
        
        for (int i = 0; i < (width / w) + w; i++) {
            g2.setPaint(getBackground());
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(0, 0, 0, height);
            g2.translate(w + increment, 0.0d);
        }
        
        if (increment < 1) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_OFF);
        
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                            RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
        }
    }
}
