/*
 * JScreenSaver.java
 *
 * Created on May 9, 2006, 3:54 PM
 */

package org.rummage.slide;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 *
 * @author nnelson
 */
public class JSplashScreen
    extends JTransparentWindow
{
    private URL resource = null;
    private Image image = null;
    private JComponent clip = null;
    
    /** Creates a new instance of JScreenSaver */
    public JSplashScreen(String imagePath) {
        super();
        
        setResource(imagePath);
        
        initUI();
    }
    
    private void initUI() {
        ImageIcon icon = new ImageIcon(resource);
        
        setImage(icon.getImage());
        
        clip = new JComponent() {
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                
                int height = getHeight();
                int width = getWidth();
                
                Color bgcolor = new Color(255, 0, 0, 100);
                
                g2.drawImage(getImage(), 0, 0, null);
                
                g2.dispose();
            }
        };
        
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        
        de.bug.ging(w + ", " + h);
        
        Dimension preferredDimension = new Dimension(w, h);
        
        clip.setPreferredSize(preferredDimension);
        clip.setMaximumSize(preferredDimension);
        clip.setMinimumSize(preferredDimension);
        
        add(clip, BorderLayout.CENTER);
        
        clip.setBounds(0, 0, w, h);
        clip.setOpaque(false);
        
        setSize(clip.getWidth(), clip.getHeight());
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void setResource(String path) {
        if (new java.io.File(path).isAbsolute()) {
            try {
                resource = new URL(path);
            }
            catch (MalformedURLException ex) {
                //ex.printStackTrace();
            }
        }
        else {
            resource = JSplashScreen.class.getResource(path);
        }
    }
    
    public void setImage(Image image) {
        this.image = image;
    }
    
    public Image getImage() {
        return image;
    }
}
