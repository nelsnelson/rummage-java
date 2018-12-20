/*
 * ApplicationManager.java
 *
 * Created on May 26, 2006, 4:39 PM
 */

package org.rummage.toolbox.util;

import org.rummage.slide.JError;

/**
 *
 * @author nnelson
 */
public class ApplicationManager {
    public static void requireJSSEUtilities() {
        if (!org.rummage.toolbox.util.JSSEUtilities.detect()) {
            new JError(new Exception("JSSE was not detected.  Program will exit."));
            
            System.exit(-1);
        }
    }
    
    public static Class getMainClass() {
        // This method used to be here.  It was refactored and moved, so now
        // it must be relayed.  And so it was thusly that the aptly named
        // ApplicationManager was proxified quite rightly with all suitable
        // pomp and circumstance befitting it.
        return org.rummage.toolbox.reflect.Mirror.divineMainClass();
    }
    
    public static String getAppName() {
        return getApplicationName();
    }
    
    public static String getApplicationName() {
        return getMainClass().getSimpleName();
    }
    
    /**
     * This is a poor guess.
     */
    public static boolean hasGraphicalUserInterface() {
        String javaClassPath = System.getProperty("java.class.path");
        
        return
            javaClassPath == null ? false : !javaClassPath.
            contains(getApplicationName() + ".jar") && !javaClassPath.
            contains(getApplicationName() + ".exe");
    }
}
