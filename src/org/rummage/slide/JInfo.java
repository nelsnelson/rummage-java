/*
 * JInfo.java
 *
 * Created on April 25, 2006, 8:32 AM
 */

package org.rummage.slide;

import java.awt.Component;

import org.rummage.toolbox.reflect.Mirror;
import org.rummage.toolbox.util.ApplicationManager;
import org.rummage.toolbox.util.Properties;

/**
 *
 * @author nelsnelson
 */
public class JInfo {
    private static final String TITLE = "Info";
    private JOptionPane info = null;
    
    /** Creates a new instance of JInfo */
    public JInfo(Exception ex) {
        this(ex, null);
    }
    
    /** Creates a new instance of JInfo */
    public JInfo(String message) {
        this(new Exception(message), null);
    }
    
    /** Creates a new instance of JInfo */
    public JInfo(String message, Component parent) {
        this(new Exception(message), parent);
    }
    
    /** Creates a new instance of JInfo */
    public JInfo(Exception ex, Component parent) {
        if (ApplicationManager.hasGraphicalUserInterface()) {
            info = new JOptionPane(ex.getMessage(), JOptionPane.PLAIN_MESSAGE);
        }
        
        if (Properties.getBoolean(Properties.SUPPRESS_ERRORS)) {
            return;
        }
        
        String message = ex.getMessage();
        
        if (message == null || message.length() == 0) {
            message = ex.toString();
        }
        
        if (parent != null) {
            parent.requestFocus();
        }
        
        if (Mirror.isExecutingWithinMainStack()) {
            if (info == null) {
                System.out.println(message);
            }
            else {
                info.showMessageDialog(parent, message, TITLE,
                    JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
    
    public static void info(String message) {
        info(message, null);
    }
    
    public static void info(String message, Component parent) {
        new JInfo(message, parent);
    }
}
