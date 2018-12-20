/*
 * RedoAction.java
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
 * @author not attributable
 */
public class RedoAction
    extends ContextAction
{
    public RedoAction() {
        super("Redo");
        
        setMnemonic('R');
    }
    
    public RedoAction(String k) {
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
            if (undoManager.canRedo()) {
                undoManager.redo();
            }
        }
        catch (javax.swing.undo.CannotRedoException ex) {
            
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
            undoManager.canRedo() &&
            target.isEditable() &&
            target.isEnabled();
    }
}
