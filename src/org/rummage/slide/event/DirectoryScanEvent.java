/*
 * DirectoryScanEvent.java
 *
 * Created on June 28, 2006, 4:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.rummage.slide.event;

import java.io.File;
import java.util.EventObject;

/**
 *
 * @author nnelson
 */
public class DirectoryScanEvent
    extends EventObject
{
    private File currentDirectory = null;
    private File selectedFile = null;
    
    /** Creates a new instance of DirectoryScanEvent */
    public DirectoryScanEvent(Object source, File currentDirectory, File selectedFile) {
        super(source);
        
        this.currentDirectory = currentDirectory;
        this.selectedFile = selectedFile;
    }
    
    public File getCurrentDirectory() {
        return currentDirectory;
    }
    
    public File getSelectedFile() {
        return selectedFile;
    }
}
