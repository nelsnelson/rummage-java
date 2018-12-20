/*
 * JTabbedPaneFactory.java
 *
 * Created on May 23, 2006, 3:37 PM
 */

package org.rummage.slide;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.plaf.ColorUIResource;

import org.rummage.toolbox.util.UIManager;

/**
 *
 * @author nnelson
 */
public class JTabbedPaneFactory {
    /** Creates a new instance of JTabbedPaneFactory */
    public JTabbedPaneFactory() {
        
    }
    
    public static JTabbedPane createJTabbedPane() {
        return createJTabbedPane(JTabbedPane.TOP, new Insets(0, 0, 0, 0),
            new Insets(4, 18, 1, 18), Color.LIGHT_GRAY);
    }
    
    public static JTabbedPane createJTabbedPane(int tabPlacement,
        Insets insets, Insets tabInsets, Color darkShadow)
    {
        Color oldShadow = UIManager.getColor("TabbedPane.darkShadow");
        Insets oldTabInsets = UIManager.getInsets("TabbedPane.tabInsets"); 
        Insets oldInsets = UIManager.getInsets("TabbedPane.contentBorderInsets"); 
        
        // bottom insets is 1 because the tabs are bottom aligned 
        UIManager.put("TabbedPane.contentBorderInsets", insets); 
        UIManager.put("TabbedPane.tabInsets", tabInsets); 
        UIManager.put("TabbedPane.darkShadow", new ColorUIResource(darkShadow));
        
        JTabbedPane tabbedPane = new JTabbedPane(tabPlacement); 
        
        UIManager.put("TabbedPane.contentBorderInsets", oldInsets); 
        UIManager.put("TabbedPane.tabInsets", oldTabInsets); 
        UIManager.put("TabbedPane.darkShadow", new ColorUIResource(oldShadow)); 
        
        return tabbedPane;
    }
}
