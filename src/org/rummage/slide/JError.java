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
 * @author not attributable
 */
public class JError {
    private static final String TITLE = "Error";
    private JOptionPane error = null;
    
    /** Creates a new instance of JError */
    public JError(Exception ex) {
        this(ex, null);
    }
    
    /** Creates a new instance of JError */
    public JError(String message) {
        this(new Exception(message), null);
    }
    
    /** Creates a new instance of JError */
    public JError(String message, Component parent) {
        this(new Exception(message), parent);
    }
    
    /** Creates a new instance of JError */
    public JError(Exception ex, Component parent) {
        if (ApplicationManager.hasGraphicalUserInterface()) {
            error = new JOptionPane(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
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
            if (error == null) {
                System.err.println(message);
            }
            else {
                error.showMessageDialog(parent, message, TITLE,
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            throw new RuntimeException(ex);
        }
    }
    
    public static void error(String message) {
        error(message, null);
    }
    
    public static void error(String message, Component parent) {
        new JError(message, parent);
    }
}
