/*
 * SelectAllAction.java
 *
 * Created on February 8, 2006, 8:15 AM
 */
package org.rummage.slide.context.actions;

import java.awt.event.ActionEvent;

import javax.swing.text.JTextComponent;

import org.rummage.slide.context.ContextAction;

/**
 *
 * @author Santhosh Kumar T - santhosh@in.fiorano.com
 */
public class SelectAllAction extends ContextAction{ 
    public SelectAllAction(){ 
        super("Select All"); 
        
        setMnemonic('A');
    } 
    
    public SelectAllAction(String k) {
        this();
        
        setAcceleratorKey(k);
    }
 
    public void actionPerformed(ActionEvent e){ 
        JTextComponent target = getFocusedComponent();
        
        if (target != null) {
            target.selectAll();
        }
    } 
 
    public boolean isEnabled(){ 
        JTextComponent target = getFocusedComponent();
        
        if (target == null) {
            return false;
        }
        
        String text = target.getText();
        String selectedText = target.getSelectedText();
        
        return
            target.isEnabled() &&
            text.length() > 0 &&
            (selectedText == null || selectedText.length() < text.length());
    } 
} 
