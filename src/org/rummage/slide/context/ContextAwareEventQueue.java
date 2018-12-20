/*
 * ContextAwareEventQueue.java
 *
 * Created on February 8, 2006, 9:16 AM
 */

package org.rummage.slide.context;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPopupMenu;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.rummage.slide.SlideUtilities;
import org.rummage.toolbox.util.UIManager;

/**
 *
 * @author Santhosh Kumar T - santhosh@in.fiorano.com 
 */
public class ContextAwareEventQueue extends EventQueue{ 
    private static ActionMap customActionMap = null;
    private static InputMap customInputMap = null;
    
    /** Creates a new instance of ContextAwareEventQueue */
    public ContextAwareEventQueue() {
        new ContextManager();
        
        initCustomUIDefaults();
    }
    
    private static void initCustomUIDefaults() {
        customInputMap = ContextManager.getCustomInputMap();
        customActionMap = ContextManager.getCustomActionMap();
        
        InputMap defaultTextAreaFocusMap =
            (InputMap) UIManager.getDefaults().get("TextArea.focusInputMap");
        
        defaultTextAreaFocusMap.setParent(customInputMap);
        
        InputMap defaultTextFieldFocusMap =
            (InputMap) UIManager.getDefaults().get("TextField.focusInputMap");
        
        defaultTextFieldFocusMap.setParent(customInputMap);
        
        InputMap defaultTextPaneFocusMap =
            (InputMap) UIManager.getDefaults().get("TextPane.focusInputMap");
        
        defaultTextPaneFocusMap.setParent(customInputMap);
        
        InputMap defaultFormattedTextFieldFocusMap =
            (InputMap) UIManager.getDefaults().get("FormattedTextField.focusInputMap");
        
        defaultFormattedTextFieldFocusMap.setParent(customInputMap);
        
        InputMap defaultFileChooserInputMap =
            (InputMap) UIManager.getDefaults().get("FileChooser.ancestorInputMap");
        
        defaultFileChooserInputMap.setParent(customInputMap);
        
        InputMap defaultEditorPaneFocusMap =
            (InputMap) UIManager.getDefaults().get("EditorPane.focusInputMap");
        
        defaultEditorPaneFocusMap.setParent(customInputMap);
        
        InputMap defaultListFocusMap =
            (InputMap) UIManager.getDefaults().get("List.focusInputMap");
        
        defaultListFocusMap.setParent(customInputMap);
        
        InputMap defaultTreeFocusMap =
            (InputMap) UIManager.getDefaults().get("Tree.focusInputMap");
        
        defaultTreeFocusMap.setParent(customInputMap);
    }
    
    protected void dispatchEvent(AWTEvent event){ 
        if (SlideUtilities.isForcingTableEdit()) {
            return;
        }
        
        try {
            super.dispatchEvent(event); 
        }
        catch (Exception ex) {
            if (UIManager.isGracefullyHandlingErrors()) {
                new org.rummage.slide.JError(ex);
            }
            
            ex.printStackTrace();
        }
        
        // focus events should always change the context
        if (event instanceof FocusEvent) {
            FocusEvent fe = (FocusEvent) event;
            
            ContextManager.focusChanged(fe);
            
            return;
        }
        
        // interested only in mouseevents 
        if(!(event instanceof MouseEvent)) 
            return; 
        
        MouseEvent me = (MouseEvent)event; 
        
        // interested only in popuptriggers 
        if(!me.isPopupTrigger()) 
            return; 
        
        // me.getComponent(...) returns the heavy weight component on which event occured 
        Component target =
            (Component) SwingUtilities.getDeepestComponentAt(me.getComponent(), me.getX(), me.getY()); 
        
        // only if target is showing
        if (target == null || !target.isShowing())
            return;
        
        target.requestFocus();
        
        Component originalTarget = null;
        
        if (target instanceof javax.swing.JTable) {
            originalTarget = target;
            target = SlideUtilities.forceEdit((javax.swing.JTable) target, me);
        }
        
        // Interested only in textcomponents 
        if (!(target instanceof JTextComponent)) 
            return; 
        
        // no popup shown by user code 
        if(MenuSelectionManager.defaultManager().getSelectedPath().length>0) 
            return; 
        
        ContextManager.setPopupTrigger(true);
        
        try {
            // create popup menu and show 
            JTextComponent tc = (JTextComponent)target; 
            
            ContextManager.focusChanged(new FocusEvent(tc, FocusEvent.FOCUS_GAINED));
            
            JPopupMenu popupMenu = new JPopupMenu();
            
            if (originalTarget instanceof javax.swing.JTable) { /* no undoing */ } else {
            popupMenu.add(ContextManager.get(tc, "Undo"));
            popupMenu.add(ContextManager.get(tc, "Redo"));
            popupMenu.addSeparator(); }
            popupMenu.add(ContextManager.get(tc, "Cut")); 
            popupMenu.add(ContextManager.get(tc, "Copy")); 
            popupMenu.add(ContextManager.get(tc, "Paste")); 
            popupMenu.add(ContextManager.get(tc, "Delete")); 
            popupMenu.addSeparator(); 
            popupMenu.add(ContextManager.get(tc, "Select All")); 

            Point pt = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(), tc);

            if (tc.isShowing()) {
                popupMenu.show(tc, pt.x, pt.y);
            }
        }
        catch (Exception ex) {
            
        }
        finally {
            ContextManager.setPopupTrigger(false);
        }
    } 
} 
