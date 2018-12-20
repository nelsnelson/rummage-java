package org.rummage.slide;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import javax.swing.Icon;
import javax.swing.KeyStroke;

public class JPopupMenu
    extends javax.swing.JPopupMenu
    implements ActionListener
{
    private List<ActionListener> actionListeners = null;
    private WeakHashMap<String, javax.swing.JMenuItem> menuItems = null;
    
    public JPopupMenu() {
        super();
        
        actionListeners = new ArrayList<ActionListener>();
        menuItems = new WeakHashMap<String, javax.swing.JMenuItem>();
    }
    
    public JMenuItem getMenuItem(String menuItemName) {
        return (JMenuItem) menuItems.get(menuItemName);
    }
    
    public void addActionListener(ActionListener actionListener) {
        actionListeners.add(actionListener);
        
        Component[] components = getComponents();
        
        for (Component c : components) {
            if (c instanceof JMenuItem) {
                JMenuItem menuItem = (JMenuItem) c;
                
                menuItem.addAllActionListeners(actionListeners);
            }
            else if (c instanceof JCheckBoxMenuItem) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) c;
                
                menuItem.addAllActionListeners(actionListeners);
            }
        }
    }
    
    public JMenuItem add(JMenuItem menuItem) {
        menuItem.addAllActionListeners(actionListeners);
        menuItems.put(menuItem.getName(), menuItem);

        return (JMenuItem) super.add(menuItem);
    }

    public JCheckBoxMenuItem add(JCheckBoxMenuItem menuItem) {
        menuItem.addAllActionListeners(actionListeners);
        menuItems.put(menuItem.getName(), menuItem);

        return (JCheckBoxMenuItem) super.add(menuItem);
    }

    public JCheckBoxMenuItem add(String text, boolean b) {
        return add(text, (char) 0, null, null, b);
    }

    public JMenuItem add(String text, char mnemonic) {
        return add(text, mnemonic, null, null);
    }

    public JCheckBoxMenuItem add(String text, char mnemonic,
        boolean b) {
        return add(text, mnemonic, null, null, b);
    }

    public JMenuItem add(String text, char mnemonic, String k) {
        return add(text, mnemonic, k, null);
    }

    public JMenuItem add(String text, Icon icon) {
        return add(text, (char) 0, null, icon);
    }

    public JCheckBoxMenuItem add(String text, Icon icon,
        boolean b)
    {
        return add(text, (char) 0, null, icon, b);
    }

    public JMenuItem add(String text, String k) {
        return add(text, (char) 0, k, null);
    }

    public JCheckBoxMenuItem add(String text, String k, boolean b) {
        return add(text, (char) 0, k, null, b);
    }

    public JMenuItem add(String text, char mnemonic, Icon icon)
    {
        return add(text, (char) 0, null, icon);
    }

    public JCheckBoxMenuItem add(String text, char mnemonic,
        Icon icon, boolean b)
    {
        return add(text, (char) 0, null, icon, b);
    }

    public JCheckBoxMenuItem add(String text, char mnemonic,
        String k, boolean b)
    {
        return add(text, (char) 0, k, null, b);
    }

    public JMenuItem add(String text, char mnemonic, String k,
        Icon icon)
    {
        return add(new JMenuItem(text, mnemonic, 
            KeyStroke.getKeyStroke(k), icon));
    }

    public JCheckBoxMenuItem add(String text, char mnemonic,
        String k, Icon icon, boolean b)
    {
        return add(new JCheckBoxMenuItem(text, mnemonic, 
            KeyStroke.getKeyStroke(k), icon, b));
    }

    public void actionPerformed(ActionEvent e) {
    }
}
