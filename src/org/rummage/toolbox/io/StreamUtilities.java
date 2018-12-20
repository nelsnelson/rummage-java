/*
 * StreamUtilities.java
 *
 * Created on November 25, 2006, 10:59 AM
 */

package org.rummage.toolbox.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 *
 * @author nnelson
 */
public class StreamUtilities {
    private StreamUtilities() {
        
    }
    
    public static void stream(InputStream in, File file) {
        int data = -1;
        
        try {
            FileOutputStream out = new FileOutputStream(file);
            
            while((data = in.read()) > -1) {
                out.write(data);
            }
            
            in.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public static byte[] stream(InputStream in) {
        int data = -1;
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try {
            while((data = in.read()) > -1) {
                out.write(data);
            }
            
            in.close();
        }
        catch (IOException ex) {
            
        }
        
        return out.toByteArray();
    }

    /**
     * Reads data from the input and writes it to the output, until the end of the input
     * stream.
     * 
     * @param in
     * @param out
     * @param bufSizeHint
     * @throws IOException
     */
    public static void copyPipe(InputStream in, OutputStream out, int bufSizeHint)
            throws IOException {
        int read = -1;
        byte[] buf = new byte[bufSizeHint];
        while ((read = in.read(buf, 0, bufSizeHint)) >= 0) {
            out.write(buf, 0, read);
        }
        out.flush();
    }
    
    public static boolean pipe(URL source, File destination) {
        BufferedInputStream fin = null;
        BufferedOutputStream fout = null;
        try {
            int bufSize = 8 * 1024;
            fin = new BufferedInputStream(source.openConnection().getInputStream(), bufSize);
            fout = new BufferedOutputStream(new FileOutputStream(destination), bufSize);
            StreamUtilities.copyPipe(fin, fout, bufSize);
        }
        catch (IOException ioex) {
            return false;
        }
        catch (SecurityException sx) {
            return false;
        }
        finally {
            if (fin != null) {
                try {
                    fin.close();
                }
                catch (IOException cioex) {
                }
            }
            if (fout != null) {
                try {
                    fout.close();
                }
                catch (IOException cioex) {
                }
            }
        }
        return true;
    }
    
    public static boolean pipe(File source, File destination) {
        BufferedInputStream fin = null;
        BufferedOutputStream fout = null;
        try {
            int bufSize = 8 * 1024;
            fin = new BufferedInputStream(new FileInputStream(source), bufSize);
            fout = new BufferedOutputStream(new FileOutputStream(destination), bufSize);
            StreamUtilities.copyPipe(fin, fout, bufSize);
        }
        catch (IOException ioex) {
            return false;
        }
        catch (SecurityException sx) {
            return false;
        }
        finally {
            if (fin != null) {
                try {
                    fin.close();
                }
                catch (IOException cioex) {
                }
            }
            if (fout != null) {
                try {
                    fout.close();
                }
                catch (IOException cioex) {
                }
            }
        }
        return true;
    }
}
