/*
 * PasteAction.java
 *
 * Created on February 8, 2006, 8:23 AM
 */
package org.rummage.slide.context.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;

import javax.swing.text.JTextComponent;

import org.rummage.slide.context.ContextAction;

/**
 *
 * @author Santhosh Kumar T - santhosh@in.fiorano.com 
 */
public class PasteAction extends ContextAction{ 
    public PasteAction(){ 
        super("Paste"); 
        
        setMnemonic('P');
    } 
    
    public PasteAction(String k) {
        this();
        
        setAcceleratorKey(k);
    }
 
    public void actionPerformed(ActionEvent e){ 
        JTextComponent target = getFocusedComponent();
        
        if (target != null) {
            target.paste(); 
        }
    } 
 
    public boolean isEnabled(){ 
        JTextComponent target = getFocusedComponent();
        
        if (target == null) {
            return false;
        }
        
        Clipboard clipboard =
            Toolkit.getDefaultToolkit().getSystemClipboard();
        
        return
            target.isEditable() &&
            target.isEnabled() &&
            clipboard.getAvailableDataFlavors().length > 0 && 
            clipboard.getContents(this).isDataFlavorSupported(DataFlavor.
            selectBestTextFlavor(clipboard.getAvailableDataFlavors()));
    } 
} 
