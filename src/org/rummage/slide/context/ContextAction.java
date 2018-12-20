/*
 * ContextAction.java
 *
 * Created on February 8, 2006, 12:38 PM
 */
package org.rummage.slide.context;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import javax.swing.undo.UndoManager;

/**
 *
 * @author not attributable
 */
public class ContextAction
    extends TextAction
{
    private Action action = null;
    
    public ContextAction(String actionName) {
        super(actionName);
    }
    
    public ContextAction(Action action) {
        super((String) action.getValue(NAME));
        
        this.action = action;
        
        putValue(Action.ACCELERATOR_KEY, action.getValue(Action.ACCELERATOR_KEY));
        putValue(Action.ACTION_COMMAND_KEY, action.getValue(Action.ACTION_COMMAND_KEY));
        putValue(Action.DEFAULT, action.getValue(action.DEFAULT));
        putValue(Action.LONG_DESCRIPTION, action.getValue(Action.LONG_DESCRIPTION));
        putValue(Action.MNEMONIC_KEY, action.getValue(Action.MNEMONIC_KEY));
        putValue(Action.SHORT_DESCRIPTION, action.getValue(Action.SHORT_DESCRIPTION));
        putValue(Action.SMALL_ICON, action.getValue(Action.SMALL_ICON));
        setEnabled(action.isEnabled());
    }
    
    public UndoManager getUndoManager(JTextComponent undoable) {
        // get the context
        if (!ContextManager.hasUndoManager(undoable)) {
            ContextManager.registerUndoable(undoable);
        }
        
        return ContextManager.getUndoManager(undoable);
    }
    
    public void setMnemonic(char mnemonic) {
        putValue(MNEMONIC_KEY, (int) mnemonic);
    }
    
    public char getMnemonic(char mnemonic) {
        return (char) ((Integer) getValue(MNEMONIC_KEY)).intValue();
    }
    
    public void setAcceleratorKey(String k) {
        if (k != null)
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyStroke.getKeyStroke(k).getKeyCode(), 
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }
    
    public KeyStroke getAcceleratorKey() {
        return (KeyStroke) getValue(ACCELERATOR_KEY);
    }
    
    public void setEnabled(boolean b) {
        if (action != null) {
            action.setEnabled(b);
        }
        
        super.setEnabled(b);
    }
    
    public boolean isEnabled() {
        return action == null ? super.isEnabled() : action.isEnabled();
    }
    
    public Object getValue(String key) {
        if (key.equals(ACCELERATOR_KEY) && ContextManager.isPopupTrigger()) {
            return null;
        }
        
        return super.getValue(key);
    }

    public void actionPerformed(ActionEvent e) {
        if (action != null) {
            action.actionPerformed(e);
        }
    }
    
    public String toString() {
        return getValue(NAME) + " " + (isEnabled() ? '\u2713' : ' ');
    }
}
