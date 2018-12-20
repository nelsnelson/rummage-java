/*
 * JMenuItem.java
 *
 * Created on February 6, 2006, 8:28 AM
 */
package org.rummage.slide;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 *
 * @author nelsnelson
 */
public class JMenuItem
    extends javax.swing.JMenuItem
{
    public JMenuItem(String text) {
        this(text, (char) 0, (KeyStroke) null, null);
    }
    
    public JMenuItem(String text, int mnemonic) {
        this(text, (char) mnemonic, (KeyStroke) null, null);
    }
    
    public JMenuItem(String text, char mnemonic) {
        this(text, mnemonic, (KeyStroke) null, null);
    }
    
    public JMenuItem(String text, Icon icon) {
        this(text, (char) 0, (KeyStroke) null, icon);
    }
    
    public JMenuItem(String text, KeyStroke k) {
        this(text, (char) 0, k, null);
    }
    
    public JMenuItem(String text, char mnemonic, Icon icon) {
        this(text, (char) 0, (KeyStroke) null, icon);
    }
    
    public JMenuItem(String text, char mnemonic, KeyStroke k) {
        this(text, (char) 0, k, null);
    }
    
    public JMenuItem(String text, char mnemonic, String s) {
        this(text, (char) 0, KeyStroke.getKeyStroke(s), null);
    }
    
    public JMenuItem(String text, char mnemonic, String s, Icon icon) {
        this(text, (char) 0, KeyStroke.getKeyStroke(s), icon);
    }
    
    public JMenuItem(String text, char mnemonic, KeyStroke k, Icon icon)
    {
        super(text);
        setName(text);
        setMnemonic(mnemonic);
        setIcon(icon);
        if (k != null)
            setAccelerator(KeyStroke.getKeyStroke(k.getKeyCode(), 
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }
    
    public void addAllActionListeners(List<ActionListener> listeners) {
        List<ActionListener> currentListeners =
            Arrays.asList(getActionListeners());
        
        for (ActionListener l : listeners) {
            if (!currentListeners.contains(l)) {
                addActionListener(l);
            }
        }
    }
}
