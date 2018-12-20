/*
 * Chrono.java
 *
 * Created on December 2, 2005, 10:47 AM
 */

package org.rummage.toolbox.util;

/**
 *
 * @author not attributable
 */
public class Chrono {
    public static long start;
    public static long finish;
    
    /** Creates a new instance of Chrono */
    public Chrono() {
        start = System.currentTimeMillis();
    }
    
    public static void start() {
        new Chrono();
    }
    
    public static long stop() {
        finish = System.currentTimeMillis() - start;
        
        return finish;
    }
    
    public static long now() {
        return System.currentTimeMillis();
    }
}
