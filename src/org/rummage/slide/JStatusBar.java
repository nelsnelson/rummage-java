/*
 * JStatusBar.java
 *
 * Created on October 24, 2005, 8:47 AM
 */

package org.rummage.slide;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.rummage.toolbox.util.MemoryMonitor;
import org.rummage.toolbox.util.Properties;
import org.rummage.info.clearthought.layout.TableLayout;

/**
 *
 * @author nelsnelson
 */
public class JStatusBar
    extends JComponent
    implements CornerPeer
{
    protected Window parent = null;
    private JPanel panel = null;
    private JPanel corner = null;
    private JGrowBox growBox = null;
    private TableLayout layout = null;
    private int i = 0;
    
    /** Creates a new instance of JStatusBar */
    public JStatusBar() {
        this(null);
    }
    
    /** Creates a new instance of JStatusBar */
    public JStatusBar(final Window parent) {
        this.parent = parent;
        
        initUI();
        
        addStuff();
    }
    
    protected void initUI() {
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        layout = new TableLayout();
        if (Properties.isWindowsXP()) {
            layout.insertRow(0, 23.0);
        }
        else if (Properties.isWindows2000()) {
            layout.insertRow(0, 21.0);
        }
        
        setLayout(layout);
        
        Dimension preferredDimension = null;
        if (Properties.isWindowsXP()) {
            preferredDimension = new Dimension(19, 23);
        }
        else if (Properties.isWindows2000()) {
            preferredDimension = new Dimension(19, 21);
        }
        
        corner = new JPanel();
        corner.setPreferredSize(preferredDimension);
        corner.setMinimumSize(preferredDimension);
        corner.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        corner.setLayout(new BorderLayout());
        corner.setOpaque(false);
        
        layout.insertColumn(0, -2.0);
        add(corner, "0, 0, 0, 0");
        
        growBox = new JGrowBox(parent);
        
        setCornerComponent(growBox);
        
        addAncestorListener(new AncestorAdapter(this) {
            public void parentChanged() {
                if (parent == null) {
                    parent = getWindow();
                    
                    growBox.setWindow(parent);
                }
            }
        });
    }
    
    public final JLabel statusMessageLabel = new JLabel();
    public final JLabel memoryMonitorLabel = new JLabel();
        
    private void addStuff() {
        Dimension preferredDimension = null;
        preferredDimension = new Dimension(120, 23);
        memoryMonitorLabel.setPreferredSize(preferredDimension);
        memoryMonitorLabel.setMaximumSize(preferredDimension);
        memoryMonitorLabel.setMinimumSize(preferredDimension);
        
        addStatusBarComponent(memoryMonitorLabel);
        addStatusBarComponent(statusMessageLabel);
        
        statusMessageLabel.setText(System.getProperty("java.runtime.name") +
            ", " + "Virtual Machine Version " +
            System.getProperty("java.vm.version"));
        
        new MemoryMonitor().
            addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt) {
                Object o = evt.getNewValue();
                
                if (o instanceof String) {
                    memoryMonitorLabel.setText(o.toString());
                }
            }
        });
    }
    
    public void addStatusBarComponent(Component comp) {
        if (getComponentCount() > 1 && Properties.isWindowsXP()) {
            JDivider divider = new JDivider();
            layout.setColumn(0, 0.22295);
            layout.insertColumn(0, -2.0);
            add(divider, "0, 0, 0, 0");
        }
        
        layout.insertColumn(0, -1.0);
        
        
        
        if (comp instanceof JComponent) {
            JComponent c = (JComponent) comp;
            c.setOpaque(false);
            c.setBorder(BorderFactory.createEmptyBorder(3, 6, 0, 0));
        }
        add(comp, "0, 0, 0, 0");
    }
    
    private BufferedImage bar = null;
    
    public void paintComponent(java.awt.Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        // create the image every time, since this component will change its shape
        bar = (BufferedImage)(this.createImage(getWidth(), getHeight()));
        
        drawWindowsXPBar(bar.createGraphics());
        
        g2.drawImage(bar, null, 0, 0);
        
        g2.dispose();
    }
    
    private void drawWindowsXPBar(Graphics g) {
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
        
        int w = getWidth();
        Color background = getBackground();
        
        g2.translate(0, 0);
        
        g2.setColor(ColorUtilities.darken(background, 0.377d));
        g2.drawLine(0, 0, w, 0);
        
        g2.translate(0, 1);
        
        g2.setColor(ColorUtilities.darken(background, 0.225d));
        g2.drawLine(0, 0, w, 0);
        
        g2.translate(0, 1);
        
        g2.setColor(ColorUtilities.darken(background, 0.075d));
        g2.drawLine(0, 0, w, 0);
        
        g2.translate(0, 1);
        
        g2.setColor(ColorUtilities.darken(background, 0.025d));
        g2.drawLine(0, 0, w, 0);
        
        g2.translate(0, 1);
        
        g2.setColor(ColorUtilities.lighten(background, 0.025d));
        g2.drawLine(0, 0, w, 0);
        
        g2.translate(0, 14);
        
        g2.setColor(ColorUtilities.darken(background, 0.010d));
        g2.drawLine(0, 0, w, 0);
        
        g2.translate(0, 1);
        
        g2.setColor(ColorUtilities.darken(background, 0.025d));
        g2.drawLine(0, 0, w, 0);
        
        g2.translate(0, 1);
        
        g2.setColor(ColorUtilities.darken(background, 0.035d));
        g2.drawLine(0, 0, w, 0);
        
        g2.translate(0, 1);
            
        g2.setColor(ColorUtilities.darken(background, 0.035d));
        g2.drawLine(0, 0, w, 0);
        
        g2.translate(0, 1);
        
        g2.setColor(ColorUtilities.darken(background, 0.020d));
        g2.drawLine(0, 0, w, 0);
        
        g2.dispose();
    }
    
    public void setVisible(boolean b) {
        super.setVisible(b);
        growBox.swapCornerPeer(this);
    }

    public JGrowBox getGrowBox() {
        return growBox;
    }
    
    public void setCornerComponent(Component corner) {
        this.corner.add(corner == null ? new JLabel() : corner,
            BorderLayout.AFTER_LINE_ENDS);
        this.corner.repaint(this.corner.getBounds());
    }
}

class JDivider
    extends JComponent
{
    public JDivider() {
        setOpaque(false);
    }
    
    public void paintComponent(java.awt.Graphics g) {
        java.awt.Graphics g2 = g.create();
        
        if (Properties.isWindows2000()) {
            Dimension preferredDimension =
                new Dimension(4, getParent().getHeight() + 2);
            
            setPreferredSize(preferredDimension);
            setMaximumSize(preferredDimension);
            setMinimumSize(preferredDimension);
            
            g2.translate(0, 0);
            
            int h = getHeight() + 4;
            int w = 4;

            g2.translate(0, -2);
            
            g2.setColor(getBackground());
            g2.drawLine(0, 0, w, 0);
            
            g2.setColor(Color.WHITE);
            g2.drawLine(0, 0, 0, h);
            
            g2.setColor(getBackground().darker());
            g2.drawLine(w-1, 0, w-1, h);
            
            g2.setColor(getBackground());
            g2.drawLine(0, h, w, h);
            
            g2.dispose();
        }
        else if (Properties.isWindowsXP()) {
            Dimension preferredDimension = new Dimension(15, 19);
            setPreferredSize(preferredDimension);
            setMaximumSize(preferredDimension);
            setMinimumSize(preferredDimension);
            
            int h = getHeight()-6;
            int w = 2;

            g2.translate(6, 3);

            g2.setColor(ColorUtilities.darken(getBackground(), 0.125d));
            g2.drawLine(0, 1, 0, h);

            g2.translate(1, 0);

            g2.setColor(Color.WHITE);
            g2.drawLine(0, 1, 0, h);

            g2.dispose();
        }
    }
}
