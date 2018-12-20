/*
 * MemoryMonitor.java
 *
 * Created on October 24, 2005, 8:49 AM
 */

package org.rummage.toolbox.util;

import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

import javax.swing.AbstractAction;

/**
 *
 * @author nelsnelson
 */
public class MemoryMonitor
    extends AbstractAction
{
    private String message = null;
    
    /** Creates a new instance of MemoryMonitor */
    public MemoryMonitor() {
        new javax.swing.Timer(1000, this).start();
    }
    
    public void actionPerformed(ActionEvent e) {
        double freeMemory = Runtime.getRuntime().freeMemory() / 1000000.0d;
        double maxMemory = Runtime.getRuntime().maxMemory() / 1000000.0d;
        double totalMemory = Runtime.getRuntime().totalMemory() / 1000000.0d;
        double usedMemory = totalMemory - freeMemory;

        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        
        setText(decimalFormat.format(usedMemory) + "MB / " +
            decimalFormat.format(maxMemory) + "MB");
    }
    
    private void setText(String text) {
        putValue(LONG_DESCRIPTION, text);
    }
    
    public String getText() {
        return (String) getValue(LONG_DESCRIPTION);
    }
}
