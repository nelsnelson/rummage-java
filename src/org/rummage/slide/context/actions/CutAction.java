/*
 * CutAction.java
 *
 * Created on February 8, 2006, 8:23 AM
 */
package org.rummage.slide.context.actions;

import java.awt.event.ActionEvent;

import javax.swing.text.JTextComponent;

import org.rummage.slide.context.ContextAction;

/**
 *
 * @author Santhosh Kumar T - santhosh@in.fiorano.com 
 */
public class CutAction extends ContextAction{ 
    public CutAction(){ 
        super("Cut"); 
        
        setMnemonic('t');
    } 
    
    public CutAction(String k) {
        this();
        
        setAcceleratorKey(k);
    }
 
    public void actionPerformed(ActionEvent e){ 
        JTextComponent target = getFocusedComponent();
        
        if (target != null) {
            target.cut(); 
        }
    } 
 
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
 