/*
 * JFatality.java
 *
 * Created on August 16, 2006, 2:02 PM
 */

package org.rummage.slide;

import java.awt.Component;

import org.rummage.toolbox.util.ApplicationManager;

/**
 *
 * @author nelsnelson
 */
public class JFatality
    extends JError
{
    private static final String FATALITY_MESSAGE =
        "\n\n" + ApplicationManager.getApplicationName() + " will now exit.";
    
    /** Creates a new instance of JFatality */
    public JFatality(Exception ex) {
        this(ex, null);
    }
    
    /** Creates a new instance of JFatality */
    public JFatality(String message) {
        this(new Exception(message), null);
    }
    
    /** Creates a new instance of JFatality */
    public JFatality(String message, Component parent) {
        this(new Exception(message), parent);
    }
    
    /** Creates a new instance of JFatality */
    public JFatality(Exception ex, Component parent) {
        super(new Exception(ex.getMessage() + FATALITY_MESSAGE, ex), parent);
        
        // Manadatory system exit on a fatal error!  Sorry...
        System.exit(-1);
    }
}
