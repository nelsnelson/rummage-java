/*
 * JBufferedCanvas.java
 *
 * Created on November 9, 2006, 6:17 AM
 */

package org.rummage.slide;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Stack;

import javax.swing.JComponent;

/**
 *
 * @author nelsnelson
 */
public class JBufferedCanvas
    extends JComponent
{
    private Graphics2D g2 = null;
    private Stack<Graphics2D> graphics = null;
    private BufferedImage image = null;
    
    public int width = 0;
    public int height = 0;
    
    /** Creates a new instance of JBufferedCanvas */
    public JBufferedCanvas() {
        super();
        
        setBackground(Color.WHITE);
        
        graphics = new Stack<Graphics2D>();
    }
    
    public void paintComponent(Graphics g) {
        push((Graphics2D) g.create());
        
        g2 = peek();
        g2.translate(getInsets().top, getInsets().left);
        
        int width = getWidth();
        int height = getHeight();
        
        // fill background, if any?
        //g2.setPaint(getBackground());
        g2.setPaint(Color.BLUE);
        g2.fill(new Rectangle2D.Double(0, 0, width, height));
        
        if (image == null) {
            image = (BufferedImage)(this.createImage(width, height));
        }
        
        draw(image);
        
        pop().dispose();
    }
    
    public void draw(BufferedImage image) {
        drawLine(0,0,width,height);
        
        push((Graphics2D) image.getGraphics());
        
        draw();
        
        pop().dispose();
        
        peek().drawImage(image, 0, image.getHeight(), image.getWidth(), 0,
            0, 0, image.getWidth(), image.getHeight(), null);
    }
    
    public void draw() {
        //stub
    }
    
    public void drawLine(int x0, int y0, int x1, int y1) {
        peek().drawLine(x0, y0, x1, y1);
    }
    
    public void translate(int x, int y) {
        peek().translate(x, y);
    }
    
    public void setStrokeColor(Color c) {
        peek().setPaint(c);
    }
    
    public void setStrokeWidth(int width) {
        peek().setStroke(new BasicStroke(width));
    }
    
    public void push(Graphics2D g2) {
        graphics.push(g2);
    }
    
    public Graphics2D pop() {
        return graphics.pop();
    }
    
    public Graphics2D peek() {
        return graphics.peek();
    }
}
