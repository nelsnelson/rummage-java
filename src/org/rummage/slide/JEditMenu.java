/*
 * JEditMenu.java
 *
 * Created on February 6, 2006, 1:58 PM
 */
package org.rummage.slide;

import java.awt.Window;

import org.rummage.slide.context.ContextManager;

/**
 *
 * @author nelsnelson
 */
public class JEditMenu
    extends JMenu
{
    /** Creates a new instance of JEditMenu */
    public JEditMenu() {
        this(null, "Edit", 'E');
    }
    
    /** Creates a new instance of JEditMenu */
    public JEditMenu(Window window) {
        this(window, "Edit", 'E');
    }
    
    /** Creates a new instance of JEditMenu */
    public JEditMenu(String menuName) {
        this(null, menuName, (char) 0);
    }
    
    /** Creates a new instance of JEditMenu */
    public JEditMenu(String menuName, char mnemonic) {
        this(null, menuName, mnemonic);
    }
    
    /** Creates a new instance of JEditMenu */
    public JEditMenu(Window window, String menuName, char mnemonic) {
        super(window, menuName, mnemonic);
        
        init();
    }
    
    private void init() {
        removeAll();
        
        add(ContextManager.get("Undo"));
        add(ContextManager.get("Redo"));
        addSeparator();
        add(ContextManager.get("Cut"));
        add(ContextManager.get("Copy"));
        add(ContextManager.get("Paste"));
        add(ContextManager.get("Delete"));
        addSeparator();
        add(ContextManager.get("Select All"));
    }
    
    public void setPopupMenuVisible(boolean b) {
        if (b) {
            init();
        }
        
        super.setPopupMenuVisible(b);
    }
}
