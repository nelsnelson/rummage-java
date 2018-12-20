/*
 * ClassUtils.java
 *
 * Created on December 13, 2006, 3:54 PM
 */

package org.rummage.toolbox.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 *
 * @author nnelson
 */
public class ClassUtils {
    private ClassUtils() {
        
    }
    
    public static Class loadClass(File classFile, String classRootPackage) {
        Class clazz = null;
        String className = classFile.getName();
        String classSpec = className.substring(0, className.lastIndexOf("."));
        
        try {
            // Convert the class root to a URL
            URL url = classFile.getParentFile().toURI().toURL();
            
            // Create a new class loader with the directory
            ClassLoader classLoader = new URLClassLoader(new URL[] { url });
            
            // Load in the class
            clazz = classLoader.loadClass(classRootPackage + "." + classSpec);
        }
        catch (MalformedURLException e) {
            clazz = e.getClass();
        }
        catch (ClassNotFoundException e) {
            clazz = e.getClass();
        }
        
        return clazz;
    }
}
