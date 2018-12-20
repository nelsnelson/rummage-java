package org.rummage.slide;

import java.awt.AWTEvent;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

/**
 * @author Santhosh Kumar T - santhosh@in.fiorano.com
 * @link http://www.javalobby.org/java/forums/m91839863.html
 * @since 26.07.05
 */
public final class ScrollGestureRecognizer implements AWTEventListener {

    private static ScrollGestureRecognizer instance = new ScrollGestureRecognizer();

    private ScrollGestureRecognizer() {
        start();
    }

    public static ScrollGestureRecognizer getInstance() {
        return instance;
    }

    void start() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this,
            AWTEvent.MOUSE_EVENT_MASK);
    }

    void stop() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
    }

    public void eventDispatched(AWTEvent event) {
        MouseEvent me = (MouseEvent) event;
        boolean isGesture = SwingUtilities.isMiddleMouseButton(me)
            && me.getID() == MouseEvent.MOUSE_PRESSED;
        if (!isGesture) {
            return;
        }

        Component comp = me.getComponent();
        comp = SwingUtilities
            .getDeepestComponentAt(comp, me.getX(), me.getY());
        JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(
            JViewport.class, comp);
        if (viewPort == null) {
            return;
        }
        JRootPane rootPane = SwingUtilities.getRootPane(viewPort);
        if (rootPane == null) {
            return;
        }

        if (viewPort.getParent() instanceof JScrollPane) {
            JScrollPane parent = (JScrollPane) viewPort.getParent();
            if (parent != null) {
                if (!(parent.getVerticalScrollBar().isVisible() || parent
                    .getHorizontalScrollBar().isVisible())) {
                    return;
                }
            }
        }

        Point location = SwingUtilities.convertPoint(me.getComponent(), me
            .getPoint(), rootPane.getGlassPane());
        ScrollGlassPane glassPane = new ScrollGlassPane(rootPane
            .getGlassPane(), viewPort, location);
        rootPane.setGlassPane(glassPane);
        glassPane.setVisible(true);
    }

    private static class ScrollGlassPane extends JPanel implements
        ActionListener, MouseInputListener, SwingConstants {

        private static final long serialVersionUID = -585424493963729515L;

        private static Image createImage() {
            BufferedImage image = new BufferedImage(30, 30,
                BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw circles
            g.setColor(Color.BLACK);
            g.fillOval(0, 0, 30, 30);
            g.setColor(Color.WHITE);
            g.fillOval(2, 2, 26, 26);
            g.setColor(Color.BLACK);
            g.fillOval(13, 13, 4, 4);

            // Draw Arrows
            g.fill(new Polygon(new int[] { 15, 11, 19 },
                new int[] { 5, 9, 9 }, 3));

            g.rotate(Math.PI / 2, 15, 15);
            g.fill(new Polygon(new int[] { 15, 11, 19 },
                new int[] { 5, 9, 9 }, 3));

            g.rotate(Math.PI / 2, 15, 15);
            g.fill(new Polygon(new int[] { 15, 11, 19 },
                new int[] { 5, 9, 9 }, 3));

            g.rotate(Math.PI / 2, 15, 15);
            g.fill(new Polygon(new int[] { 15, 11, 19 },
                new int[] { 5, 9, 9 }, 3));

            return image;
        }

        private static final Image          IMAGE        = createImage();
        private static final AlphaComposite ALPHA        = AlphaComposite
                                                             .getInstance(
                                                                 AlphaComposite.SRC_OVER,
                                                                 0.7f);

        protected Component                 oldGlassPane = null;
        protected Point                     location     = null;

        private Timer                       movingTimer;
        private Point                       mouseLocation;
        private JViewport                   viewport;

        public ScrollGlassPane(Component oldGlassPane, JViewport viewport,
            Point location) {
            this.oldGlassPane = oldGlassPane;
            this.viewport = viewport;
            this.location = location;
            mouseLocation = location;

            setOpaque(false);

            ScrollGestureRecognizer.getInstance().stop();
            addMouseListener(this);
            addMouseMotionListener(this);

            movingTimer = new Timer(100, this);
            movingTimer.setRepeats(true);
            movingTimer.start();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g.setComposite(ALPHA);
            g.drawImage(IMAGE, location.x - 15, location.y - 15, this);
        }

        /*---------------------[ ActionListener ]----------------------*/

        public void actionPerformed(ActionEvent e) {
            int deltax = (mouseLocation.x - location.x) / 4;
            int deltay = (mouseLocation.y - location.y) / 4;

            Point p = viewport.getViewPosition();
            p.translate(deltax, deltay);

            if (p.x < 0) {
                p.x = 0;
            } else
                if (p.x >= viewport.getView().getWidth() - viewport.getWidth()) {
                    p.x = viewport.getView().getWidth() - viewport.getWidth();
                }

            if (p.y < 0) {
                p.y = 0;
            } else
                if (p.y >= viewport.getView().getHeight()
                    - viewport.getHeight()) {
                    p.y = viewport.getView().getHeight()
                        - viewport.getHeight();
                }

            viewport.setViewPosition(p);
        }

        /*----------------------[ MouseListener ]-----------------------*/

        public void mousePressed(MouseEvent e) {
            movingTimer.stop();
            setVisible(false);
            JRootPane rootPane = SwingUtilities.getRootPane(this);
            rootPane.setGlassPane(oldGlassPane);
            ScrollGestureRecognizer.getInstance().start();
        }

        public void mouseClicked(MouseEvent e) {
            mousePressed(e);
        }

        public void mouseMoved(MouseEvent e) {
            mouseLocation = e.getPoint();
        }

        public void mouseDragged(MouseEvent e) {
            // Nothing to do
        }

        public void mouseEntered(MouseEvent e) {
            // Nothing to do
        }

        public void mouseExited(MouseEvent e) {
            // Nothing to do
        }

        public void mouseReleased(MouseEvent e) {
            // Nothing to do
        }
    }

}