/*
 * ContextManager.java
 *
 * Created on February 28, 2006, 10:37 AM
 */

package org.rummage.slide.context;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

import org.rummage.slide.JEditMenu;
import org.rummage.slide.context.actions.CopyAction;
import org.rummage.slide.context.actions.CutAction;
import org.rummage.slide.context.actions.DeleteAction;
import org.rummage.slide.context.actions.PasteAction;
import org.rummage.slide.context.actions.RedoAction;
import org.rummage.slide.context.actions.SelectAllAction;
import org.rummage.slide.context.actions.UndoAction;

/**
 *
 * @author nelsnelson
 */
public class ContextManager {
    private static Map<JTextComponent, PausableUndoManager> undoManagers = null;
    
    private static ActionMap customActionMap = null;
    private static InputMap customInputMap = null;
    
    private static UndoAction undoAction = null;
    private static RedoAction redoAction =  null;
    private static CutAction cutAction = null;
    private static CopyAction copyAction = null;
    private static PasteAction pasteAction = null;
    private static DeleteAction deleteAction = null;
    private static SelectAllAction selectAllAction = null;
    
    @SuppressWarnings("unused")
    private static JEditMenu editMenu = null;

    private static boolean popupTrigger;
    
    public ContextManager() {
        undoManagers = getUndoManagers();
        
        undoAction = new UndoAction("Z");
        redoAction = new RedoAction("Y");
        cutAction = new CutAction("X");
        copyAction = new CopyAction("C");
        pasteAction = new PasteAction("V");
        deleteAction = new DeleteAction();
        selectAllAction = new SelectAllAction("A");
        
        initCustomActionMap();
        initCustomInputMap();
    }
    
    public static boolean hasUndoManager(JTextComponent textComponent) {
        return getUndoManagers().containsKey(textComponent);
    }
    
    public static UndoManager getUndoManager(JTextComponent textComponent) {
        return getUndoManagers().get(textComponent);
    }
    
    public static Map<JTextComponent, PausableUndoManager> getUndoManagers() {
        if (undoManagers == null) {
            undoManagers = new WeakHashMap<JTextComponent, PausableUndoManager>();
        }
        
        return undoManagers;
    }

    public static void pauseUndoManager(JTextComponent textComponent) {
        if (hasUndoManager(textComponent)) {
            PausableUndoManager undoManager =
                (PausableUndoManager) getUndoManager(textComponent);
            
            if (undoManager != null) {
                undoManager.pause();
            }
        }
    }

    public static void resumeUndoManager(JTextComponent textComponent) {
        if (hasUndoManager(textComponent)) {
            PausableUndoManager undoManager =
                (PausableUndoManager) getUndoManager(textComponent);
            
            if (undoManager != null) {
                undoManager.resume();
            }
        }
    }

    public static void removeUndoManager(JTextComponent textComponent) {
        if (hasUndoManager(textComponent)) {
            UndoManager undoManager = getUndoManager(textComponent);
            
            undoManager.discardAllEdits();
            undoManager.end();
            undoManager.die();
            undoManager = null;
            
            getUndoManagers().remove(textComponent);
        }
    }

    public static void resetUndoManager(JTextComponent textComponent) {
        removeUndoManager(textComponent);
        registerUndoable(textComponent);
    }
    
    public static void registerUndoable(JTextComponent textComponent) {
        if (undoManagers.containsKey(textComponent) ||
            textComponent == null)
        {
            return;
        }
        
        javax.swing.text.Document d = textComponent.getDocument();
        
        if (d != null) {
            PausableUndoManager undoManager = new PausableUndoManager();
            
            d.addUndoableEditListener(undoManager);
            getUndoManagers().put(textComponent, undoManager);
        }
    }
    
    public static void setEditMenu(JEditMenu menu) {
        editMenu = menu;
    }
    
    public static ActionMap getCustomActionMap() {
        if (customActionMap == null) {
            initCustomActionMap();
        }
        
        return customActionMap;
    }
    
    public static InputMap getCustomInputMap() {
        if (customInputMap == null) {
            initCustomInputMap();
        }
        
        return customInputMap;
    }
    
    private static void initCustomActionMap() {
        customActionMap = new ActionMap();
        
        customActionMap.put(undoAction.getValue(Action.NAME), undoAction);
        customActionMap.put(redoAction.getValue(Action.NAME), redoAction);
        customActionMap.put(cutAction.getValue(Action.NAME), cutAction);
        customActionMap.put(copyAction.getValue(Action.NAME), copyAction);
        customActionMap.put(pasteAction.getValue(Action.NAME), pasteAction);
        customActionMap.put(deleteAction.getValue(Action.NAME), deleteAction);
        customActionMap.put(selectAllAction.getValue(Action.NAME), selectAllAction);
    }
    
    private static void initCustomInputMap() {
        customInputMap = new InputMap();
        
        Action copyAction = TransferHandler.getCopyAction();
        Action pasteAction = TransferHandler.getPasteAction();
        Action cutAction = TransferHandler.getCutAction();
        
        customInputMap.put(undoAction.getAcceleratorKey(), undoAction);
        customInputMap.put(redoAction.getAcceleratorKey(), redoAction);
        customInputMap.put(KeyStroke.getKeyStroke("control shift Z"), redoAction);
        
        customInputMap.put((KeyStroke) copyAction.getValue(Action.ACCELERATOR_KEY), copyAction);
        customInputMap.put((KeyStroke) pasteAction.getValue(Action.ACCELERATOR_KEY), pasteAction);
        customInputMap.put((KeyStroke) cutAction.getValue(Action.ACCELERATOR_KEY), cutAction);
        
        customInputMap.put(selectAllAction.getAcceleratorKey(), selectAllAction);
    }
    
    public static Component getTheFuckingFocusOwner() {
        java.awt.KeyboardFocusManager currentKeyboardFocusManager =
            getFocusManager();
        
        java.awt.Window w = currentKeyboardFocusManager.getFocusedWindow();
        
        java.awt.Component c = null;
        
        if (w == null) {
            c = currentKeyboardFocusManager.getFocusOwner();
        }
        else {
            c = w.getFocusOwner();
        }
        
        if (c == null) {
            c = currentKeyboardFocusManager.getFocusOwner();
        }
        
        if (c == null || !c.isShowing()) {
            return w;
        }
        
        return c;
    }
    
    public static java.awt.Window getFocusedWindow() {
        java.awt.KeyboardFocusManager currentKeyboardFocusManager =
            getFocusManager();
        
        if (currentKeyboardFocusManager != null) {
            return currentKeyboardFocusManager.getFocusedWindow();
        }
        
        return null;
    }
    
    public static java.awt.KeyboardFocusManager getFocusManager() {
        return java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager();
    }
    
    public static ContextAction get(String actionName) {
        Component focusOwner = getTheFuckingFocusOwner();
        
        if (customActionMap == null) {
            return null;
        }
        
        if (focusOwner instanceof JTextComponent) {
            
            JTextComponent tc = (JTextComponent) focusOwner;

            return get(tc, actionName);
        }
        
        ContextAction a = (ContextAction) customActionMap.get(actionName);
        KeyStroke k = a.getAcceleratorKey();
        if (k != null)
            a.setAcceleratorKey(KeyStroke.getKeyStroke(k.getKeyCode(), 
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()).toString());
        
        a.setEnabled(false);
        
        return a;
    }

    public static ContextAction get(JTextComponent tc, String actionName) {
        InputMap inputMap = tc.getInputMap();
        
        Action a = customActionMap.get(actionName);
        
        if (a == null) {
            return null;
        }
        
        ((ContextAction) a).setEnabled(tc != null && ((ContextAction) a).isEnabled());
        
        KeyStroke key = (KeyStroke) a.getValue(Action.ACCELERATOR_KEY);
        
        if (key == null) {
            return (ContextAction) a;
        }
        
        Object o = inputMap.get(key);
        
        if (o instanceof ContextAction) {
            return (ContextAction) o;
        }
        else if (a instanceof ContextAction) {
            return (ContextAction) a;
        }
        
        return null;
    }
    
    public static void updateContext(JTextComponent context) {
        InputMap inputMap = context == null ? null : context.getInputMap();
        
        if (inputMap != null && inputMap.size() > 0) {
            for (KeyStroke key : inputMap.keys()) {
                Object o = inputMap.get(key);
                
                if (o instanceof Action) {
                    Action a = (Action) o;
                    
                    a.setEnabled(context != null && a.isEnabled());
                }
            }
        }
        
        for (KeyStroke key : customInputMap.keys()) {
            Object o = customInputMap.get(key);
            
            if (o instanceof ContextAction) {
                ContextAction a = (ContextAction) o;
                
                a.setEnabled(a.isEnabled());
            }
        }
        
        for (Object key : customActionMap.keys()) {
            Object o = customActionMap.get(key);
            
            if (o instanceof ContextAction) {
                ContextAction a = (ContextAction) o;
                
                a.setEnabled(a.isEnabled());
            }
        }
    }

    public static void focusChanged(FocusEvent e) {
        Object o = getTheFuckingFocusOwner();
        
        if (o instanceof JTextComponent) {
            JTextComponent tc = (JTextComponent) o;
        
            updateContext(tc);
        }
        
        o = e.getComponent();
        
        if (o instanceof JTextComponent) {
            JTextComponent tc = (JTextComponent) o;
        
            updateContext(tc);
        }
        
        updateContext(null);
    }

    public static void setPopupTrigger(boolean b) {
        popupTrigger = b;
    }

    public static boolean isPopupTrigger() {
        return popupTrigger;
    }
}

class PausableUndoManager
    extends UndoManager
{
    /**
     * 
     */
    private static final long serialVersionUID = 743169375829024204L;

    private boolean paused = false;

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public void undoableEditHappened(UndoableEditEvent e) {
        if (!paused) addEdit(e.getEdit());
    }
}
