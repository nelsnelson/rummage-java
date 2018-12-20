/*
 * FileTransferHandler.java
 *
 * Created on August 15, 2006, 2:46 PM
 */

package org.rummage.toolbox.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JTextArea;

import org.rummage.slide.dnd.TransferDelegate;

/**
 *
 * @author nnelson
 */
public class FileTransferHandler
    implements TransferDelegate
{
    private JTextArea textArea = null;
    private boolean showProgress = false;
    
    /** Creates a new instance of FileTransferHandler */
    public FileTransferHandler(JTextArea textArea) {
        this(textArea, false);
    }
    
    public FileTransferHandler(JTextArea textArea, boolean showProgress) {
        this.textArea = textArea;
        this.showProgress = showProgress;
    }
    
    private final void loadFile0(File file) {
        loadFile(file);
    }
    
    // override this
    public void loadFile(final File file) {
        try {
            FileManager.loadFileText(file, textArea, FileManager.NO_VISUAL);
        }
        catch (FileNotFoundException ex) {
            //ex.printStackTrace();
        }
        catch (IOException ex) {
            //ex.printStackTrace();
        }
    }
    
    public boolean handleTransfer(Object data) {
        try {
            if (data instanceof String) {
                String s = data.toString().trim();
                
                File file = null;
                
                if (s.startsWith("file://")) {
                    loadFile0(new File(new URI(s)));
                    
                    return true;
                }
            }
            else if (data instanceof List) {
                for (Object o : (List) data) {
                    File file = new File(o.toString());
                    
                    loadFile0(file);
                }
                
                return true;
            }
        }
        catch (URISyntaxException ex) {
            return false;
        }
        
        return false;
    }
}
