/*
 * JEditPopupMenu.java
 *
 * Created on February 3, 2006, 3:29 PM
 */
package org.rummage.slide;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

/**
 *
 * @author not attributable
 */
public class JEditPopupMenu
    extends JContextPopupMenu
{
    private static JTextComponent target = null;
    private static UndoManager undoManager = null;
    
    /** Creates a new instance of JEditPopupMenu */
    public JEditPopupMenu() {
        this(null);
    }
    
    /** Creates a new instance of JEditPopupMenu */
    public JEditPopupMenu(JTextComponent invoker) {
        super(invoker);
        
        init();
        
        setTarget(invoker);
    }
    
    private void init() {
        add("Undo", 'U');
        
        add("Redo", 'R');

        addSeparator();
        
        add("Cut", 't');
        
        add("Copy", 'C');
        
        add("Paste", 'P');
        
        add("Delete", 'D');

        addSeparator();
        
        add("Select All", 'A');
        
        addActionListener(this);
    }
    
    public void setTarget(JTextComponent target) {
        if (target == null) {
            return;
        }
        
        this.target = target;
        
        Document d = target.getDocument();
        
        if (d != null) {
            if (undoManager == null) {
                undoManager = new UndoManager();
            }
            
            d.addUndoableEditListener(undoManager);
        }
    }
    
    public JTextComponent getTarget() {
        return target;
    }
    
    public UndoManager getUndoManager() {
        return undoManager;
    }

    protected void update() {
        if (target == null) {
            return;
        }
        
        if (undoManager == null) {
            return;
        }
        
        // update the Undo menu item
        getMenuItem("Undo").setEnabled(undoManager.canUndo() &&
            target.isEnabled() && target.isEditable());

        // update the Redo menu item
        getMenuItem("Redo").setEnabled(undoManager.canRedo() &&
            target.isEnabled() && target.isEditable());
        
        String selectedText = target.getSelectedText();
        String text = target.getText();
        
        // update the Cut menu item
        getMenuItem("Cut").setEnabled(target.isEnabled() &&
            target.isEditable() &&
            selectedText != null && selectedText.length() > 0);

        // update the Copy menu item
        getMenuItem("Copy").setEnabled(target.isEnabled() &&
            selectedText != null && selectedText.length() > 0);

        // update the Paste menu item
        try {
            java.awt.datatransfer.Clipboard clipboard =
                Toolkit.getDefaultToolkit().getSystemClipboard();
            
            getMenuItem("Paste").setEnabled(target.isEditable() &&
                target.isEnabled() &&
                clipboard.getContents(null).getTransferData(DataFlavor.
                selectBestTextFlavor(clipboard.getAvailableDataFlavors())).
                toString().length() > 0);
        }
        catch (HeadlessException ex) {

        }
        catch (IOException ex) {

        }
        catch (UnsupportedFlavorException ex) {

        }
        catch (IllegalStateException ex) {

        }

        // update the Delete menu item
        getMenuItem("Delete").setEnabled(target.isEnabled() &&
            target.isEditable() &&
            selectedText != null && selectedText.length() > 0);

        // update the Select All menu item
        getMenuItem("Select All").setEnabled(target.isEnabled() &&
            text.length() > 0 && (selectedText == null ||
            selectedText.length() < text.length()));
    }

    public void actionPerformed(ActionEvent e) {
        if (target == null) {
            return;
        }
        
        String command = e.getActionCommand();
        
        if (command.equals("Undo")) {
            maybeUndo();
        }
        else if (command.equals("Redo")) {
            maybeRedo();
        }
        else if (command.equals("Cut")) {
            target.cut();
        }
        else if (command.equals("Copy")) {
            target.copy();
        }
        else if (command.equals("Paste")) {
            target.paste();
        }
        else if (command.equals("Delete")) {
            target.replaceSelection(null);
        }
        else if (command.equals("Select All")) {
            target.selectAll();
        }
    }
    
    private void maybeUndo() {
        if (target.isEditable() && target.isEnabled()) {
            if (undoManager != null && undoManager.canUndo()) {
                undoManager.undo();
            }
        }
    }
    
    private void maybeRedo() {
        if (target.isEditable() && target.isEnabled()) {
            if (undoManager != null && undoManager.canRedo()) {
                undoManager.redo();
            }
        }
    }
}
