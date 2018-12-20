/*
 * BufferedReaderFactory.java
 *
 * Created on August 2, 2006, 12:16 PM
 */

package org.rummage.toolbox.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author nnelson
 */
public class BufferedReaderFactory {
    private static BufferedReaderFactory singletonFactory = null;
    
    public static BufferedReaderFactory getInstance() {
        if (singletonFactory == null) {
            singletonFactory = create();
        }
        
        return singletonFactory;
    }
    
    private static BufferedReaderFactory create() {
        return new BufferedReaderFactory();
    }
    
    public BufferedReader getReader(String source) {
        if (source == null) {
            return null;
        }
        
        try {
            return getReader(new URL(source));
        }
        catch (MalformedURLException ex) {
            return getReader(new File(source));
        }
    }
    
    public BufferedReader getReader(File source) {
        try {
            return getReader(source.toURI().toURL());
        }
        catch (NullPointerException ex) {
            return null;
        }
        catch (MalformedURLException ex) {
            return null;
        }
    }
    
    public BufferedReader getReader(URL source) {
        try {
            InputStream stream = source.openStream();
            
            InputStreamReader reader = new InputStreamReader(stream);
            
            return new BufferedReader(reader);
        }
        catch (NullPointerException ex) {
            return null;
        }
        catch (IOException ex) {
            return null;
        }
    }
}
