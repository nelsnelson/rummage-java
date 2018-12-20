/*
 * JTitleBar.java
 *
 * Created on April 28, 2006, 1:02 PM
 */

package org.rummage.slide;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

/*
 * JTitleBar.java
 *
 * Created on April 28, 2006, 1:02 PM
 * @author not attributable
 */
public class JTitleBar
    extends JComponent
{
    /*** displays title information ***/
    JLabel title = null;
    JMiniCloseButton closeButton = null;
    
    /** Creates a new instance of JTitleBar */
    public JTitleBar() {
        this(null, "Title Bar");
    }
    
    /** Creates a new instance of JTitleBar */
    public JTitleBar(JPanel c, String text) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(ColorUtilities.
            fade(Color.GRAY, ColorUtilities.lighten(Color.BLUE, 0.90d), 0.90d)));
        
        JComponent titleBar = new JComponent() {};
        titleBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JComponent controls = new JComponent() {};
        controls.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        title = new JLabel(text);
        title.setFont(new Font("Tahoma", Font.BOLD, 11));
        title.setAntialiasText(true);
        
        closeButton = new JMiniCloseButton(c);
        
        titleBar.add(title);
        controls.add(closeButton);
        
        add(titleBar, BorderLayout.CENTER);
        add(controls, BorderLayout.EAST);
        
        addMouseListener(new DragAdapter(this, DragAdapter.NO_BORDER,
            DragAdapter.DRAGS_PARENT));
    }
    
    public void close(java.awt.Component c) {
        c.setVisible(false);
    }
    
    public void setTitle(String text) {
        title.setText(text);
    }
    
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        int height = getHeight();
        int width = getWidth();
        
        Color c0 = ColorUtilities.darken(getBackground(), 0.10d);
        Color c1 = ColorUtilities.lighten(getBackground(), 0.70d);
        
        GradientPaint paint = new GradientPaint(0, 0, c0, width, 0, c1);
        
        g2.setPaint(paint);
        g2.fill(getBounds());
        
        g2.dispose();
    }
}
