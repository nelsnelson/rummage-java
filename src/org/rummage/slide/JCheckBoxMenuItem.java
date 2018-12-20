/*
 * JCheckBoxMenuItem.java
 *
 * Created on February 6, 2006, 8:29 AM
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
public class JCheckBoxMenuItem
    extends javax.swing.JCheckBoxMenuItem
{
    public JCheckBoxMenuItem(String text, boolean b) {
        this(text, (char) 0,  null, null, b);
    }

    public JCheckBoxMenuItem(String text, char mnemonic, boolean b) {
        this(text, mnemonic, null, null, b);
    }

    public JCheckBoxMenuItem(String text, Icon icon, boolean b)
    {
        this(text, (char) 0, null, icon, b);
    }

    public JCheckBoxMenuItem(String text, KeyStroke k, boolean b) {
        this(text, (char) 0, k, null, b);
    }

    public JCheckBoxMenuItem(String text, char mnemonic, Icon icon, boolean b)
    {
        this(text, (char) 0, null, icon, b);
    }

    public JCheckBoxMenuItem(String text, char mnemonic, KeyStroke k,
        boolean b)
    {
        this(text, (char) 0, k, null, b);
    }

    public JCheckBoxMenuItem(String text, char mnemonic, KeyStroke k,
        Icon icon, boolean b)
    {
        super(text);
        setName(text);
        setMnemonic(mnemonic);
        setIcon(icon);
        if (k != null)
            setAccelerator(KeyStroke.getKeyStroke(k.getKeyCode(), 
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        setState(b);
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

    public void toggleCheckBox() {
        setState(!getState());
    }
}
