/*
 * bug.java
 *
 * Created on December 1, 2005, 2:50 PM
 */

package de;

import org.rummage.toolbox.reflect.Mirror;

/**
 *
 * @author not attributable
 */
public class bug {
    public static bug bug = null;
    public static java.io.PrintStream ger = new java.io.PrintStream(System.out);
    
    /** Creates a new instance of bug */
    public bug() {
        this.bug = this;
    }
    
    public static bug get() {
        if (bug == null) {
            bug = replicate();
        }
        
        return bug;
    }
    
    private static bug replicate() {
        bug bug = new bug();
        bug.bug = bug;
        bug.ger = ger;
        bug.b = b;
        bug.e = e;

        return bug;
    }
    
    private static boolean b = true;
    private static boolean e = false;
    
    public static bug go() {
        b = true;
        
        return get();
    }
    
    public static bug stop() {
        b = false;
        e = false;
        
        return get();
    }
    
    public static bug trace() {
        e = true;
        
        return get();
    }
    
    public static Object ging() {
        if (b) System.out.println();
        if (e) System.err.println(Mirror.glance(get()));
        
        return null;
    }
    
    // jussa wrappa yo thingie ina dis to see its value in stdout
    public static <T> T ging(T o) {
        if (b) System.out.println(o);
        if (e) System.err.println(Mirror.glance(get()));
        
        return o;
    }
    
    // jussa wrappa yo thingie ina dis to see its value in a window
    public static <T> T ginger(T o) {
        if (o == null) {
            return o;
        }
        
        if (b) org.rummage.slide.Controller.error(new Exception(o.toString()));
        if (e) System.err.println(Mirror.glance(get()));
        
        return o;
    }
}
