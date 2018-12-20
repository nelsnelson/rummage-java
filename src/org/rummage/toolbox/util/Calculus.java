/*
 * Calculus.java
 *
 * Created on April 7, 2006, 9:53 AM
 */

package org.rummage.toolbox.util;

/**
 *
 * @author nnelson
 */
public class Calculus {
    /** Creates a new instance of Calculus */
    public Calculus() {
        
    }
    
    public static int constrain(int n, int min, int max) {
        return Math.min(Math.max(n, min), max);
    }
}
