/*
 * JMiniCloseButton.java
 *
 * Created on July 21, 2006, 4:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.rummage.slide;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.UIResource;

/**
 *
 * @author nelsnelson
 */
public class JMiniCloseButton
    extends JButton
    implements UIResource, ActionListener, AncestorListener
{
    private Component c = null;
    
    /** Creates a new instance of JMiniCloseButton */
    public JMiniCloseButton(Component c) {
        this.c = c;
        
        addActionListener(this);
        setToolTipText("Close");
        setBorder(BorderFactory.createEmptyBorder());
        setIcon(new ImageIcon(JButton.class.getResource("images/Windows_XP_Small_Window_Close_Icon.png")));
        setPressedIcon(new ImageIcon(JButton.class.getResource("images/Windows_XP_Small_Window_Close_Mouse_Clicked_Icon.png")));
        setRolloverIcon(new ImageIcon(JButton.class.getResource("images/Windows_XP_Small_Window_Close_Mouse_Over_Icon.png")));
        
        Dimension d =
            new Dimension(getIcon().getIconWidth(),
            getIcon().getIconHeight());
        
        setPreferredSize(d);
        setMaximumSize(d);
        setMinimumSize(d);
        
        //remove the typical padding for the button
        setMargin(new Insets(0,0,0,0));
        
        addAncestorListener(this);
    }
    
    public void close() {
        c.setVisible(false);
    }
    
    public void ancestorAdded(AncestorEvent event) {
        c = c == null ? getRootPane().getParent() : c;
    }
    
    public void ancestorRemoved(AncestorEvent event) {
    }
    
    public void ancestorMoved(AncestorEvent event) {
    }
    
    public void actionPerformed(ActionEvent e) {
        if (c == null) {
            return;
        }
        
        if (c instanceof java.awt.Frame) {
            ((java.awt.Frame)c).dispose();
            
            return;
        }
        else {
            close();
        }
        
        c.paint(c.getGraphics());
    }
}
