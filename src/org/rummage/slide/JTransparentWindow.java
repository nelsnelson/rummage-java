package org.rummage.slide;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 *
 * @author nelsnelson
 */
public class JTransparentWindow
    extends JWindow
{
    private Robot robot = null;
    private BufferedImage screenImg = null;
    private Rectangle screenRect = null;
    private TransparentPanel contentPanel = null;
    private boolean userActivate = true;
    
    public JTransparentWindow() {
        createScreenImage();
        
        initUI();
        
        setupListeners();
    }
    
    private void initUI() {
        contentPanel = new TransparentPanel();
        
        setContentPane(contentPanel);
    }
    
    /**
     * Try to ensure that contained components are children of the transparent
     * content frame, instead of replacing it.
     */
    public Component add(Component c) {
        getContentPane().add(c, BorderLayout.CENTER); return c;
    }
    
    public Component add(Component c, String pos) {
        getContentPane().add(c, pos); return c;
    }
    
    protected void createScreenImage() {
        try {
            if (robot==null)
                robot=new Robot();
        }
        catch (AWTException ex) {
            ex.printStackTrace();
        }
        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        screenRect=new Rectangle(0,0,screenSize.width,screenSize.height);
        screenImg=robot.createScreenCapture(screenRect);
    }
    
    public void resetUnderImg() {
        if (robot!=null && screenImg!=null) {
            Rectangle frameRect=getBounds();
            int x=frameRect.x;
            contentPanel.paintX=0;
            contentPanel.paintY=0;
            if (x<0) {
                contentPanel.paintX=-x;
                x=0;
            }
            int y=frameRect.y;
            if (y<0) {
                contentPanel.paintY=-y;
                y=0;
            }
            int w=frameRect.width;
            if (x+w>screenImg.getWidth())
                w=screenImg.getWidth()-x;
            int h=frameRect.height;
            if (y+h>screenImg.getHeight())
                h=screenImg.getHeight()-y;
            
            h = h > 0 ? h : 1;
            
            if (w>0 && h>0)
            contentPanel.underFrameImg=screenImg.getSubimage(x,y,w,h);
        }
    }
    
    private void setupListeners() {
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                resetUnderImg();
                contentPanel.repaint();
            }
            
            public void componentMoved(ComponentEvent e) {
                resetUnderImg();
                contentPanel.repaint();
            }
            
            public void componentShown(ComponentEvent e) {
                resetUnderImg();
                contentPanel.repaint();
            }
            
            public void componentHidden(ComponentEvent e) {
                resetUnderImg();
                contentPanel.repaint();
            }
        });
        
        addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                if (userActivate) {
                    userActivate=false;
                    JTransparentWindow.this.setVisible(false);
                    createScreenImage();
                    JTransparentWindow.this.setVisible(true);
                    resetUnderImg();
                }
                else {
                    userActivate=true;
                }
            }
        });
    }
}

class TransparentPanel
    extends JPanel
{
    BufferedImage underFrameImg;
    int paintX=0;
    int paintY=0;
    
    public TransparentPanel() {
        setLayout(new BorderLayout());
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(underFrameImg,paintX,paintY,null);
    }
}
