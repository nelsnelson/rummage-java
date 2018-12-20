/*
 * System.java
 *
 * Created on October 19, 2005, 5:04 PM
 */

package org.rummage.toolbox.util;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import java.awt.MenuContainer;
import java.awt.Toolkit;
import java.util.Collections;
import java.util.List;
import javax.swing.LookAndFeel;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import org.rummage.slide.context.ContextAwareEventQueue;

/**
 *
 * @author not attributable
 */
public class UIManager
    extends javax.swing.UIManager
{
    public static void setLookAndFeelToDefaultSystemLookAndFeel() {
        try {
            setLookAndFeel(getSystemLookAndFeelClassName());
        }
        catch (IllegalAccessException ex) {

        }
        catch (InstantiationException ex) {

        }
        catch (ClassNotFoundException ex) {

        }
        catch (UnsupportedLookAndFeelException ex) {

        }
        
        // get the name of the operating system
        String OS_NAME = System.getProperty("os.name");
        
        if (OS_NAME.equals("Windows 2000")) {
            
        }
        else if (OS_NAME.equals("Windows XP")) {
            initCustomWindowsXPUIDefaults();
        }
    }
    
    private static void initCustomWindowsXPUIDefaults() {
        put("PopupMenu.border", createPopupMenuWindowsXPBorder());
        
        put("PopupMenuSeparatorUI",
            "org.rummage.slide.plaf.windows.xp.BasicPopupMenuSeparatorUI");
        
        put("MenuItemUI",
            "org.rummage.slide.plaf.windows.xp.WindowsMenuItemUI");
        
        //put("TabbedPane.contentBorderInsets", new java.awt.Insets(0, 0, 0, 0)); 
    }
    
    private static Border createPopupMenuWindowsXPBorder() {
        Border b0 = (Border) get("PopupMenu.border");
        
        Border b1 = BorderFactory.createEmptyBorder(2, 2, 2, 2);
        
        Border b2 = BorderFactory.createCompoundBorder(b0, b1);
        
        return b2;
    }
    
    public static void enableProcessingAware() {
        EventQueue waitQueue = new WaitCursorEventQueue(1000);
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(waitQueue);
    }
    
    public static void enableTextAntiAliasingOverride() {
        getDefaults().put("Application.useSystemFontSettings", "false");
    }
    
    public static void enableGracefulErrorNotification() {
        getDefaults().put("Application.gracefulErrorNotification", "true");
    }
    
    public static void enableContextPopupMenus() {
        EventQueue contextQueue = new ContextAwareEventQueue();
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(contextQueue); 
    }
    
    private static void printUIDefaults(java.io.PrintStream out) {
        UIDefaults defaults = javax.swing.UIManager.getDefaults();
        
        List keys = Text.listify(defaults.keys());
        
        Collections.sort(keys);
        
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        //System.out.println("<!DOCTYPE UIDefaults SYSTEM \"http://java.sun.com/dtd/properties.dtd\">");
        out.println("<UIDefaults>");
        
        for (Object key : keys) {
            out.println("    <entry key=\"" + key + "\">" + defaults.get(key) + "</entry>");
        }
        
        out.println("</UIDefaults>");
    }
    
    static class WaitCursorEventQueue
        extends EventQueue
    {
        private int delay;
        private WaitCursorTimer waitTimer;
        
        public WaitCursorEventQueue(int delay) {
            this.delay = delay;
            waitTimer = new WaitCursorTimer();
            waitTimer.setDaemon(true);
            waitTimer.start();
        }

        protected void dispatchEvent(AWTEvent event) {
             waitTimer.startTimer(event.getSource());
            try {
                super.dispatchEvent(event);
            }
            finally {
                waitTimer.stopTimer();
            }
        }

        private class WaitCursorTimer
            extends Thread
        {
            private Object source;
            private Component parent;
            
            synchronized void startTimer(Object source) {
                this.source = source;
                notify();
            }
            
            synchronized void stopTimer() {
                if (parent == null)
                    interrupt();
                else {
                    parent.setCursor(null);
                    parent = null;
                }
            }
            
            public synchronized void run() {
                while (true) {
                    try {
                        // wait for notification from startTimer()
                        wait();
                        
                        // wait for event processing to reach the threshold,
                        // or interruption from stopTimer()
                        wait(delay);
                        
                        if (source instanceof Component) {
                            parent =
                                SwingUtilities.getRoot((Component)source);
                        }
                        else if (source instanceof java.awt.MenuComponent) {
                            java.awt.MenuContainer mParent =
                                ((java.awt.MenuComponent)source).getParent();
                            
                            if (mParent instanceof Component) {
                                parent =
                                    SwingUtilities.
                                    getRoot((Component) mParent);
                            }
                        }
                        
                        if (parent != null && parent.isShowing()) {
                            parent.setCursor(
                                    Cursor.getPredefinedCursor(
                                        Cursor.WAIT_CURSOR));
                        }
                    }
                    catch (InterruptedException ie) {
                        
                    }
                }
            }
        }
    }
    
    public static Component getParent(Component c) {
        if (c == null || c instanceof java.awt.Frame) {
            return c;
        }
        else {
            return getParent(c.getParent());
        }
    }
    
    public static boolean isGracefullyHandlingErrors() {
        Object o = getDefaults().get("Application.gracefulErrorNotification");
        
        return o == null ? false : o.equals("true");
    }
    
    public static void listKeyBindings(javax.swing.JComponent component) {
        // List keystrokes in the WHEN_FOCUSED input map of the component
        javax.swing.InputMap map = component.getInputMap(javax.swing.JComponent.WHEN_FOCUSED);
        list(map, map.keys());
        // List keystrokes in the component and in all parent input maps
        list(map, map.allKeys());
        
        // List keystrokes in the WHEN_ANCESTOR_OF_FOCUSED_COMPONENT input map of the component
        map = component.getInputMap(javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        list(map, map.keys());
        // List keystrokes in all related input maps
        list(map, map.allKeys());
        
        // List keystrokes in the WHEN_IN_FOCUSED_WINDOW input map of the component
        map = component.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        list(map, map.keys());
        // List keystrokes in all related input maps
        list(map, map.allKeys());
    }
    
    public static void list(javax.swing.InputMap map, javax.swing.KeyStroke[] keys) {
        if (keys == null) {
            return;
        }
        for (int i=0; i<keys.length; i++) {
            // This method is defined in e859 Converting a KeyStroke to a String
            String keystrokeStr = org.rummage.slide.SlideUtilities.keyStroke2String(keys[i]);

            // Get the action name bound to this keystroke
            while (map.get(keys[i]) == null) {
                map = map.getParent();
            }
            if (map.get(keys[i]) instanceof String) {
                String actionName = (String)map.get(keys[i]);
                System.out.println(keys[i] + " :: " + actionName);
            } else {
                javax.swing.Action action = (javax.swing.Action)map.get(keys[i]);
                System.out.println(keys[i] + " :: " + action);
            }
            
        }
    }
}
