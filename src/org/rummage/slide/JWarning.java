/*
 * JError.java
 *
 * Created on November 2, 2005, 4:29 PM
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
public class JWarning {
    private static final String TITLE = "Warning";
    private JOptionPane warning = null;
    
    /** Creates a new instance of JError */
    public JWarning(Exception ex) {
        this(ex, null);
    }
    
    /** Creates a new instance of JError */
    public JWarning(String message) {
        this(new Exception(message), null);
    }
    
    /** Creates a new instance of JError */
    public JWarning(String message, Component parent) {
        this(new Exception(message), parent);
    }
    
    /** Creates a new instance of JError */
    public JWarning(Exception ex, Component parent) {
        if (ApplicationManager.hasGraphicalUserInterface()) {
            warning = new JOptionPane(ex.getMessage(), JOptionPane.WARNING_MESSAGE);
        }
        
        if (Properties.getBoolean(Properties.SUPPRESS_WARNINGS)) {
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
            if (warning == null) {
                System.err.println(message);
            }
            else {
                warning.showMessageDialog(parent, message, TITLE,
                    JOptionPane.WARNING_MESSAGE);
            }
        }
        else {
            throw new RuntimeException(ex);
        }
    }
    
    public static void warning(String message) {
        warning(message, null);
    }
    
    public static void warning(String message, Component parent) {
        new JWarning(message, parent);
    }
}
