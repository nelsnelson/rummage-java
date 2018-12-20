/*
 * UndoAction.java
 *
 * Created on February 8, 2006, 8:24 AM
 */
package org.rummage.slide.context.actions;

import java.awt.event.ActionEvent;

import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

import org.rummage.slide.context.ContextAction;

/**
 *
 * @author nelsnelson
 */
public class UndoAction
    extends ContextAction
{
    public UndoAction() {
        super("Undo");
        
        setMnemonic('U');
    }
    
    public UndoAction(String k){
        this();
        
        setAcceleratorKey(k);
    }
 
    public void actionPerformed(ActionEvent e) {
        JTextComponent target = getFocusedComponent();
        
        if (target == null) {
            return;
        }
        
        UndoManager undoManager = getUndoManager(target);
        
        try {
            if (undoManager.canUndo()) {
                undoManager.undo();
            }
        }
        catch (javax.swing.undo.CannotUndoException ex) {
            
        }
    }

    public boolean isEnabled() {
        JTextComponent target = getFocusedComponent();
        
        if (target == null) {
            return false;
        }
        
        UndoManager undoManager = getUndoManager(target);
        
        return
            undoManager != null &&
            undoManager.canUndo() &&
            target.isEditable() &&
            target.isEnabled();
    }
}
