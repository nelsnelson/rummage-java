/*
 * DeleteAction.java
 *
 * Created on February 8, 2006, 8:24 AM
 */
package org.rummage.slide.context.actions;

import java.awt.event.ActionEvent;

import javax.swing.text.JTextComponent;

import org.rummage.slide.context.ContextAction;

/**
 *
 * @author Santhosh Kumar T - santhosh@in.fiorano.com 
 */
public class DeleteAction extends ContextAction{ 
    public DeleteAction(){ 
        super("Delete"); 
        
        setMnemonic('l');
    } 
 
    public void actionPerformed(ActionEvent e){ 
        JTextComponent target = getFocusedComponent();
        
        if (target != null) {
            target.replaceSelection(null); 
        }
    } 
 
    /**
     * sdfgsdfg
     */
    public boolean isEnabled(){ 
        JTextComponent target = getFocusedComponent();
        
        if (target == null) {
            return false;
        }
        
        return
            target.isEditable() &&
            target.isEnabled() &&
            target.getSelectedText()!=null; 
    } 
} 