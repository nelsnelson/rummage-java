/*
 * JMenu.java
 *
 * Created on January 30, 2006, 4:23 PM
 */
package org.rummage.slide;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.rummage.toolbox.util.Text;

/**
 *
 * @author not attributable
 */
public class JMenu
    extends javax.swing.JMenu
    implements ActionListener, WindowFocusListener
{
    private List<ActionListener> actionListeners = null;
    private HashMap<String, javax.swing.JMenuItem> menuItems = null;
    protected List<Action> actions = new ArrayList();
    
    public JMenu(String menuName) {
        this(null, menuName, (char) 0);
    }
    
    public JMenu(String menuName, char mneumonic) {
        this(null, menuName, mneumonic);
    }
    
    public JMenu(String menuName, Vector<String> menuItems) {
        this(null, menuName, (char) 0);
        
        add(menuItems);
    }
    
    public JMenu(String... items) {
        this(null, items[0], (char) 0);
        
        Vector menuItems = Text.vectorize(items);
        
        menuItems.remove(0);
        
        add(menuItems);
    }
    
    /** Creates a new instance of JMenu */
    public JMenu(Window window, String menuName, char mneumonic) {
        super(menuName);
        setMnemonic(mneumonic);
        
        actionListeners = new java.util.ArrayList<ActionListener>();
        menuItems = new HashMap<String, javax.swing.JMenuItem>();
        
        setWindow(window);
    }
    
    public void setWindow(Window window) {
        if (window != null) {
            window.addWindowFocusListener(this);
        }
    }
    
    public javax.swing.JMenuItem getMenuItem(String menuItemName) {
        return menuItems.get(menuItemName);
    }
    
    public void addActionListener(ActionListener actionListener) {
        actionListeners.add(actionListener);
        
        Component[] components = this.getMenuComponents();
        
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
    
    public javax.swing.JMenuItem add(Action a) {
        actions.add(a);
        
        if (a == null) {
            return null;
        }
        
        return (javax.swing.JMenuItem) menuItems.put(a.getValue(Action.NAME).toString(), super.add(a));
    }
    
    public JMenuItem add(JMenuItem menuItem) {
        menuItem.addAllActionListeners(actionListeners);
        menuItems.put(menuItem.getText(), menuItem);
        actions.add(menuItem.getAction());

        return (JMenuItem) super.add(menuItem);
    }

    public JCheckBoxMenuItem add(JCheckBoxMenuItem menuItem) {
        menuItem.addAllActionListeners(actionListeners);
        menuItems.put(menuItem.getText(), menuItem);
        actions.add(menuItem.getAction());

        return (JCheckBoxMenuItem) super.add(menuItem);
    }
    
    public JMenuItem insert(JMenuItem menuItem, int index) {
        return (JMenuItem) super.insert(menuItem, index);
    }
    
    public JMenuItem add(Vector<String> menuItemNames) {
        JMenuItem menuItem = null;
        
        if (menuItemNames == null) {
            return menuItem;
        }
        
        for (String menuItemName : menuItemNames) {
            menuItem = add(menuItemName);
        }
        
        return menuItem;
    }

    public JMenuItem add(String text) {
        if (text.equals("-")) {
            addSeparator();
            
            return null;
        }
        
        return add(text, (char) 0, null, null);
    }

    public JCheckBoxMenuItem add(String text, boolean b) {
        return add(text, (char) 0, null, null, b);
    }

    public JMenuItem add(String text, char mnemonic) {
        return add(text, mnemonic, null, null);
    }

    public JMenuItem add(String text, int mnemonic) {
        return add(text, (char) mnemonic, null, null);
    }

    public JCheckBoxMenuItem add(String text, char mnemonic, boolean b) {
        return add(text, mnemonic, null, null, b);
    }

    public JMenuItem add(String text, int mnemonic, String k) {
        return add(text, (char) mnemonic, k, null);
    }

    public JMenuItem add(String text, char mnemonic, String k) {
        return add(text, mnemonic, k, null);
    }

    public JMenuItem add(String text, Icon icon) {
        return add(text, (char) 0, null, icon);
    }

    public JCheckBoxMenuItem add(String text, Icon icon, boolean b) {
        return add(text, (char) 0, null, icon, b);
    }

    public JMenuItem add(String text, String k) {
        return add(text, (char) 0, k, null);
    }

    public JCheckBoxMenuItem add(String text, String k, boolean b) {
        return add(text, (char) 0, k, null, b);
    }

    public JMenuItem add(String text, char mnemonic, Icon icon) {
        return add(text, (char) 0, null, icon);
    }

    public JCheckBoxMenuItem add(String text, char mnemonic, Icon icon,
        boolean b)
    {
        return add(text, (char) 0, null, icon, b);
    }

    public JCheckBoxMenuItem add(String text, char mnemonic,
        String k, boolean b)
    {
        return add(text, (char) 0, k, null, b);
    }

    public JMenuItem add(String text, char mnemonic, String k, Icon icon) {
        return
            add(new JMenuItem(text, mnemonic, KeyStroke.getKeyStroke(k),
            icon));
    }

    public JCheckBoxMenuItem add(String text, char mnemonic, String k,
        Icon icon, boolean b)
    {
        return
            add(new JCheckBoxMenuItem(text, mnemonic, KeyStroke.
            getKeyStroke(k), icon, b));
    }

    public JMenuItem insert(String text, char mnemonic, String k, int index) {
        return
            insert(new JMenuItem(text, mnemonic, KeyStroke.getKeyStroke(k)),
            index);
    }

    public void actionPerformed(ActionEvent e) {
    }

    public void windowGainedFocus(WindowEvent e) {
        setForeground(Color.BLACK);
    }

    public void windowLostFocus(WindowEvent e) {
        setForeground(getBackground().darker());
    }

    public static JMenu getInvoker(ActionEvent e) {
        Object o = e.getSource();
        
        if (o instanceof javax.swing.JMenuItem) {
            javax.swing.JMenuItem menuItem = (javax.swing.JMenuItem) o;
            
            javax.swing.JPopupMenu parent = (javax.swing.JPopupMenu) menuItem.getParent();
            
            return (JMenu) parent.getInvoker();
        }
        
        return null;
    }

    public static String getInvokerName(ActionEvent e) {
        return getInvoker(e).getText();
    }

    public static int getIndex(ActionEvent e) {
        Object o = e.getSource();
        
        if (o instanceof javax.swing.JMenuItem) {
            javax.swing.JMenuItem menuItem = (javax.swing.JMenuItem) o;
            
            return Arrays.asList(getInvoker(e).menuItems.values()).indexOf(menuItem);
        }
        
        return -1;
    }
}
