/**
 * JGrowBox.java
 *
 * Created on July 28, 2005, 1:54 PM
 */
package org.rummage.slide;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author nelsnelson
 */
public class JGrowBox
    extends JComponent
{
    // the associated JFrame window parent of the grow box
    private Window window = null;
    
    private JGripComponent grip = null;
    
    public JGrowBox() {
        this(null, 16, 23);
    }
    
    public JGrowBox(final Window window) {
        this(window, 16, 23);
    }
    
    /** Creates a new instance of JGrowBox */
    public JGrowBox(final Window window, int width, int height) {
        this.window = window;
        
        System.setProperty("sun.awt.noerasebackground", "true");
        
        setLayout(new BorderLayout());
        
        Dimension preferredDimension = new Dimension(16, 23);
        setPreferredSize(preferredDimension);
        setMaximumSize(preferredDimension);
        setMinimumSize(preferredDimension);
        setFocusable(false);
        setOpaque(false);
        
        // Add the grip graphical component to the grow box component -- this
        // ensures that the grip always gets painted at the bottom-most region
        // of the grow box.  This separates the user-control region from the
        // gui of the grow box.
        add(createGrip(), BorderLayout.SOUTH);
        
        // getting the first mouseClick and changing the cursor and stuff
        addMouseListener(getAGrip());
        
        // when the user drags the mouse, resize the parent JFrame (ugly!)
        addMouseMotionListener(getAGrip());
        
        // hide the grow icon when the parent window is maximized
        if (window != null) {
            window.addComponentListener(getAGrip());
        }
    }
    
    public void setWindow(Window window) {
        this.window = window;
    }
    
    private JGripComponent createGrip() {
        grip = new JGripComponent();
        
        return grip;
    }
    
    private JGripComponent getAGrip() {
        return grip;
    }
    
    public void paintComponent(Graphics g) {
        if (isVisible()) {
            add(getAGrip(), BorderLayout.SOUTH);
        }
        
        super.paintComponent(g);
    }
    
    class JGripComponent
        extends JComponent
        implements ComponentListener, MouseListener, MouseMotionListener
    {
        // the image of the grow box thinga-ma-jigger
        private java.awt.Image image = null;
        
        private BufferedImage box = null;
        
        // the original window bounds
        private java.awt.Rectangle bounds0 = null;
        
        // where the user clicked the mouse to resize using the grow box
        private java.awt.Point p0 = null;
        
        private Dimension preferredDimension = new Dimension(16, 16);
        
        public JGripComponent() {
            super();
            setOpaque(false);
            
            setPreferredSize(preferredDimension);
            setMaximumSize(preferredDimension);
            setMinimumSize(preferredDimension);
        
            // getting the first mouseClick and changing the cursor and stuff
            addMouseListener(this);

            // when the user drags the mouse, resize the parent JFrame (ugly!)
            addMouseMotionListener(this);

            // hide the grow icon when the parent window is maximized
            if (window == null) {
                return;
            }
            
            window.addComponentListener(this);
        }
        
        public void mouseDragged(MouseEvent e) {
            Point loc = getLocation();
            Point p1 = e.getPoint();
            Point p2 = new Point();

            if (loc.x > 0 || loc.y > 0) {
                p2.x = window.getWidth() + (p1.x - p0.x);
                p2.y = window.getHeight() + (p1.y - p0.y);
            }
            else {
                p2.x = p1.x + 22;
                p2.y = p1.y + 58;
            }

            window.setSize(p2.x, p2.y);
            
            window.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
        }

        public void mouseEntered(MouseEvent e) {
            window.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
        }

        public void mouseExited(MouseEvent e) {
            window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        public void mouseMoved(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                p0 = e.getPoint();
                bounds0 = window.getBounds();
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (!contains(e.getPoint())) {
                window.setCursor(Cursor.
                    getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void componentResized(ComponentEvent e) {
            if (window instanceof java.awt.Frame &&
                ((java.awt.Frame) window).getExtendedState() == JFrame.
                MAXIMIZED_BOTH)
            {
                setVisible(false);
            }
            else {
                setVisible(true);
            }
        }

        public void componentMoved(ComponentEvent e) {
        }

        public void componentShown(ComponentEvent e) {
        }

        public void componentHidden(ComponentEvent e) {
        }

        public boolean contains(Point p) {
            Rectangle r = getBounds();

            return (p.x >= 0 && p.x <= r.width && p.y >= 0 && p.y <= r.height);
        }
        
        private boolean isWindows2000() {
            return System.getProperty("os.name").equals("Windows 2000");
        }
        
        private boolean isWindowsXP() {
            return System.getProperty("os.name").equals("Windows XP");
        }
        
        public int getContextualInset() {
            return getParent() instanceof JScrollPane ? 14 : 12;
        }
        
        public void paintComponent(java.awt.Graphics g) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            
            drawGrowBox(g2);
            
            g2.dispose();
        }

        private void drawGrowBox(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            
            g2.setColor(ColorUtilities.getTranslucent(Color.WHITE, 0.0d));
            g2.fill(new Rectangle(getBounds()));
            
            int d = getContextualInset();
            
            g2.translate((getParent().getWidth() - getX()) - d,
                (getParent().getHeight() - getY()) - d);
            
            drawGrip(g2);
            
            g2.dispose();
        }

        private void drawGrip(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            if (/*true) {//*/isWindows2000()) {
                drawRidges(g2);
            }
            else if (isWindowsXP()) {
                g2.translate(1, 1);
                g2.setColor(Color.WHITE);
                drawDots(g2);

                g2.translate(-1, -1);
                g2 = (Graphics2D) g;
                g2.setColor(new Color(184, 180, 161));
                drawDots(g2);
            }
            g2.dispose();
        }

        private void drawDots(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.translate(8, 0);
            g2.fill(new Rectangle(2, 2));

            g2.translate(-4, 4);
            g2.fill(new Rectangle(2, 2));

            g2.translate(4, 0);
            g2.fill(new Rectangle(2, 2));

            g2.translate(-8, 4);
            g2.fill(new Rectangle(2, 2));

            g2.translate(4, 0);
            g2.fill(new Rectangle(2, 2));

            g2.translate(4, 0);
            g2.fill(new Rectangle(2, 2));

            g2.dispose();
        }

        private void drawRidges(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.translate(-4, -4);
            g2.setColor(new Color(128, 128, 128));
            g2.drawLine(4, 14, 14, 4);
            g2.drawLine(5, 14, 14, 5);
            g2.drawLine(8, 14, 14, 8);
            g2.drawLine(9, 14, 14, 9);
            g2.drawLine(12, 14, 14, 12);
            g2.drawLine(13, 14, 14, 13);

            g2.setColor(Color.WHITE);
            g2.drawLine(3, 14, 14, 3);
            g2.drawLine(7, 14, 14, 7);
            g2.drawLine(11, 14, 14, 11);

            g2.dispose();
        }
    };
    
    CornerPeer cornerPeer = null;
    
    public void swapCornerPeer(CornerPeer c) {
        if (cornerPeer == null) {
            return;
        }
        
        cornerPeer.setCornerComponent(c.isVisible() ? null : getAGrip());
        paintImmediately(getBounds());
    }
    
    public void setCornerPeer(CornerPeer c) {
        cornerPeer = c;
    }
}
