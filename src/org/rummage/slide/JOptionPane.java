/*
 * JOptionPane.java
 *
 * Created on March 10, 2006, 11:27 AM
 */

package org.rummage.slide;

import java.awt.Component;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JRootPane;

/**
 *
 * @author nelsnelson
 */
public class JOptionPane
    extends javax.swing.JOptionPane
{
    private static Object specialValue1 = null;
    private static Object specialValue2 = null;
    
    /**
     * Creates a <code>JOptionPane</code> with a test message.
     */
    public JOptionPane() {
        this("JOptionPane message");
    }

    /**
     * Creates a instance of <code>JOptionPane</code> to display a
     * message using the 
     * plain-message message type and the default options delivered by
     * the UI.
     *
     * @param message the <code>Object</code> to display
     */
    public JOptionPane(Object message) {
        this(message, PLAIN_MESSAGE);
    }

    /**
     * Creates an instance of <code>JOptionPane</code> to display a message
     * with the specified message type and the default options,
     *
     * @param message the <code>Object</code> to display
     * @param messageType the type of message to be displayed:
     *                  <code>ERROR_MESSAGE</code>,
     *			<code>INFORMATION_MESSAGE</code>,
     *			<code>WARNING_MESSAGE</code>,
     *                  <code>QUESTION_MESSAGE</code>,
     *			or <code>PLAIN_MESSAGE</code>
     */
    public JOptionPane(Object message, int messageType) {
        this(message, messageType, DEFAULT_OPTION);
    }

    /**
     * Creates an instance of <code>JOptionPane</code> to display a message
     * with the specified message type and options.
     *
     * @param message the <code>Object</code> to display
     * @param messageType the type of message to be displayed:
     *                  <code>ERROR_MESSAGE</code>,
     *			<code>INFORMATION_MESSAGE</code>,
     *			<code>WARNING_MESSAGE</code>,
     *                  <code>QUESTION_MESSAGE</code>,
     *			or <code>PLAIN_MESSAGE</code>
     * @param optionType the options to display in the pane:
     *                  <code>DEFAULT_OPTION</code>, <code>YES_NO_OPTION</code>,
     *			<code>YES_NO_CANCEL_OPTION</code>,
     *                  <code>OK_CANCEL_OPTION</code>
     */
    public JOptionPane(Object message, int messageType, int optionType) {
        this(message, messageType, optionType, null);
    }

    /**
     * Creates an instance of <code>JOptionPane</code> to display a message
     * with the specified message type, options, and icon.
     *
     * @param message the <code>Object</code> to display
     * @param messageType the type of message to be displayed:
     *                  <code>ERROR_MESSAGE</code>,
     *			<code>INFORMATION_MESSAGE</code>,
     *			<code>WARNING_MESSAGE</code>,
     *                  <code>QUESTION_MESSAGE</code>,
     *			or <code>PLAIN_MESSAGE</code>
     * @param optionType the options to display in the pane:
     *                  <code>DEFAULT_OPTION</code>, <code>YES_NO_OPTION</code>,
     *			<code>YES_NO_CANCEL_OPTION</code>,
     *                  <code>OK_CANCEL_OPTION</code>
     * @param icon the <code>Icon</code> image to display
     */
    public JOptionPane(Object message, int messageType, int optionType,
                       Icon icon) {
        this(message, messageType, optionType, icon, null);
    }

    /**
     * Creates an instance of <code>JOptionPane</code> to display a message
     * with the specified message type, icon, and options.
     * None of the options is initially selected.
     * <p>
     * The options objects should contain either instances of
     * <code>Component</code>s, (which are added directly) or
     * <code>Strings</code> (which are wrapped in a <code>JButton</code>).
     * If you provide <code>Component</code>s, you must ensure that when the
     * <code>Component</code> is clicked it messages <code>setValue</code>
     * in the created <code>JOptionPane</code>.
     *
     * @param message the <code>Object</code> to display
     * @param messageType the type of message to be displayed:
     *                  <code>ERROR_MESSAGE</code>, 
     *			<code>INFORMATION_MESSAGE</code>,
     *			<code>WARNING_MESSAGE</code>,
     *                  <code>QUESTION_MESSAGE</code>,
     *			or <code>PLAIN_MESSAGE</code>
     * @param optionType the options to display in the pane:
     *                  <code>DEFAULT_OPTION</code>,
     *			<code>YES_NO_OPTION</code>,
     *			<code>YES_NO_CANCEL_OPTION</code>,
     *                  <code>OK_CANCEL_OPTION</code>
     * @param icon the <code>Icon</code> image to display
     * @param options  the choices the user can select
     */
    public JOptionPane(Object message, int messageType, int optionType,
                       Icon icon, Object[] options) {
        this(message, messageType, optionType, icon, options, null);
    }

    /**
     * Creates an instance of <code>JOptionPane</code> to display a message
     * with the specified message type, icon, and options, with the 
     * initially-selected option specified.
     *
     * @param message the <code>Object</code> to display
     * @param messageType the type of message to be displayed:
     *                  <code>ERROR_MESSAGE</code>,
     *			<code>INFORMATION_MESSAGE</code>,
     *			<code>WARNING_MESSAGE</code>,
     *                  <code>QUESTION_MESSAGE</code>,
     *			or <code>PLAIN_MESSAGE</code>
     * @param optionType the options to display in the pane:
     *                  <code>DEFAULT_OPTION</code>,
     *			<code>YES_NO_OPTION</code>,
     *			<code>YES_NO_CANCEL_OPTION</code>,
     *                  <code>OK_CANCEL_OPTION</code>
     * @param icon the Icon image to display
     * @param options  the choices the user can select
     * @param initialValue the choice that is initially selected; if
     *			<code>null</code>, then nothing will be initially selected;
     *			only meaningful if <code>options</code> is used
     */
    public JOptionPane(Object message, int messageType, int optionType,
                       Icon icon, Object[] options, Object initialValue) {
                       
        super(message, messageType, optionType, icon, options, initialValue);
    }
    
    @SuppressWarnings({"deprecation"})
    public static int showOptionDialog(Component parentComponent,
        Object message, String title, int optionType, int messageType,
        Icon icon, Object[] options, Object initialValue, boolean centered)
        throws HeadlessException
    {
        Window parent = (Window) parentComponent;
        Frame rootFrame = getRootFrame();
        
        final javax.swing.JOptionPane pane =
            new javax.swing.JOptionPane(message, messageType, optionType,
            icon, options, initialValue);
        
        pane.setMessageType(messageType);
        pane.setInitialValue(initialValue);
        pane.setComponentOrientation(((parentComponent == null) || centered ?
	     rootFrame : parentComponent).getComponentOrientation());
         
        JDialog dialog = pane.createDialog(parentComponent, title);
        
        WindowAdapter windowListener = new WindowAdapter() {
            public void windowDeiconified(WindowEvent e) {
                pane.grabFocus();
            }

            public void windowActivated(WindowEvent e) {
                pane.grabFocus();
            }
        };
        
        WindowFocusAdapter windowFocusListener = new WindowFocusAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                pane.grabFocus();
            }
        };
        
        if (parent == null) {
            rootFrame.addWindowListener(windowListener);
            rootFrame.addWindowFocusListener(windowFocusListener);
        }
        else {
            parent.addWindowListener(windowListener);
            parent.addWindowFocusListener(windowFocusListener);
        }
        
        pane.selectInitialValue();
        dialog.show();
        dialog.dispose();
        
        Object        selectedValue = pane.getValue();
        
        if(selectedValue == null)
            return CLOSED_OPTION;
        if(options == null) {
            if(selectedValue instanceof Integer)
                return ((Integer)selectedValue).intValue();
            return CLOSED_OPTION;
        }
        for(int counter = 0, maxCounter = options.length;
            counter < maxCounter; counter++) {
            if(options[counter].equals(selectedValue))
                return counter;
        }
        return CLOSED_OPTION;
    }
    
    public static void showInputDialog(Component parentComponent,
        Object message, String title, int messageType, Icon icon,
        Object[] selectionValues, Object initialSelectionValue, int... choices)
        throws HeadlessException
    {
        specialValue1 =
            showInputDialog(parentComponent, message, title, messageType, icon,
            selectionValues, initialSelectionValue);
    }

    public Object showInputDialog(Component parentComponent, Object message,
        String title, int messageType, Object initialSelectionValue)
        throws HeadlessException
    {
        specialValue1 =
            showInputDialog(parentComponent, message, title, messageType, null,
            null, initialSelectionValue);
        
        return specialValue1;
    }
    
    private static int styleFromMessageType(int messageType) {
        switch (messageType) {
        case ERROR_MESSAGE:
            return JRootPane.ERROR_DIALOG;
        case QUESTION_MESSAGE:
            return JRootPane.QUESTION_DIALOG;
        case WARNING_MESSAGE:
            return JRootPane.WARNING_DIALOG;
        case INFORMATION_MESSAGE:
            return JRootPane.INFORMATION_DIALOG;
        case PLAIN_MESSAGE:
        default:
            return JRootPane.PLAIN_DIALOG;
        }
    }

    public static Object getValue1() {
        return specialValue1;
    }

    public static Object getValue2() {
        return specialValue2;
    }
}

abstract class WindowFocusAdapter
    implements WindowFocusListener
{
    public void windowGainedFocus(WindowEvent e) {}

    public void windowLostFocus(WindowEvent e) {}
}
