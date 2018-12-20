/*
 * JSSEDetector.java
 *
 * Created on May 8, 2006, 2:52 PM
 */

package org.rummage.toolbox.util;

/**
 *
 * @author nelsnelson
 */
public class JSSEUtilities {
    public static boolean detect() {
        try {
            Class.forName("com.sun.net.ssl.internal.ssl.Provider");
        }
        catch (Exception e) {
            System.err.println("Error: JSSE was not detected!");
            
            return false;
        }
        
        return true;
    }
}
