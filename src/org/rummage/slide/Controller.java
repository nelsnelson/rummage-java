/*
 * Controller.java
 *
 * Created on October 19, 2005, 4:15 PM
 */

package org.rummage.slide;

import java.awt.Cursor;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.event.CaretEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;

import org.rummage.slide.actions.ActionManager;
import org.rummage.slide.actions.EasyAction;
import org.rummage.slide.dnd.TransferDelegate;

/**
 *
 * @author not attributable
 */
public abstract class Controller<T>
    extends ActionManager
    implements TransferDelegate
{
    public static Controller controller = null;
    protected static DefaultView view = null;
    protected static DefaultModel model = null;
    
    /** Creates a new instance of Controller */
    public Controller() {
        this.controller = this;
    }
    
    public static Controller getController() {
        if (controller == null) {
            controller = new Controller() {
                public void doCommand(Command command) {
                    
                }
            };
        }
        
        return controller;
    }
    
    public static void setBusy(boolean busy) {
        if (view == null) {
            return;
        }
        
        if (busy) {
            view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
        else {
            view.setCursor(null);
        }
    }
    
    protected void loadFile(File file) {
        // stub
    }

    protected void exit() {
        if (view == null) {
            System.exit(0);
        }
        else {
            view.dispose();
        }
    }
    
    protected abstract void doCommand(Command command);
    
    public void about() {
        view.showAboutDialog();
    }
    
    public static void error(Exception ex) {
        if (ex == null || ex.getClass() == Exception.class) {
            return;
        }
        
        new JError(ex, view);
    }
    
    public static void warning(Exception ex) {
        if (ex == null || ex.getClass() == Exception.class) {
            return;
        }
        
        new JWarning(ex, view);
    }
    
    public synchronized void display(String message) {
        // stub
    }
    
    public synchronized void display(String message, boolean immediately) {
        // stub
    }
    
    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        if (e == null) {
            return;
        }
        
        doCommand0(new Command(e.getActionCommand(), e.getSource()));
    }

    final private void doCommand0(Command command) {
        if (command.equals("Exit")) {
            view.windowClosing(null);
        }
        else if (command.equals("Tool Bar")) {
            view.toolBar.setVisible(!view.toolBar.isVisible());
        }
        else if (command.equals("Status Bar")) {
            view.statusBar.setVisible(!view.statusBar.isVisible());
        }
        else if (command.equals("About " + view.getApplicationTitleString())) {
            about();
        }
        else {
            doCommand(command);
        }
    }
    
    public boolean handleTransfer(Object data) {
        return false;
    }

    public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
        javax.swing.tree.TreePath path = e.getPath();
        javax.swing.tree.DefaultMutableTreeNode node =
            (javax.swing.tree.DefaultMutableTreeNode)
            path.getLastPathComponent();
        
        org.w3c.dom.Element element =
            (org.w3c.dom.Element) node.getUserObject();
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     */
    public void mouseClicked(MouseEvent e) {}

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent e) {}

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent e) {}

    /**
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent e) {}

    /**
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent e) {}

    /**
     * Invoked when a mouse button is pressed on a component and then 
     * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be 
     * delivered to the component where the drag originated until the 
     * mouse button is released (regardless of whether the mouse position 
     * is within the bounds of the component).
     * <p> 
     * Due to platform-dependent Drag&Drop implementations, 
     * <code>MOUSE_DRAGGED</code> events may not be delivered during a native 
     * Drag&Drop operation.  
     */
    public void mouseDragged(MouseEvent e) {}

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     */
    public void mouseMoved(MouseEvent e) {}

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of 
     * a key typed event.
     */
    public void keyTyped(KeyEvent e) {}

    /**
     * Invoked when a key has been pressed. 
     * See the class description for {@link KeyEvent} for a definition of 
     * a key pressed event.
     */
    public void keyPressed(KeyEvent e) {}

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of 
     * a key released event.
     */
    public void keyReleased(KeyEvent e) {}

    /**
     * Called while a drag operation is ongoing, when the mouse pointer enters
     * the operable part of the drop site for the <code>DropTarget</code>
     * registered with this listener. 
     * 
     * @param dtde the <code>DropTargetDragEvent</code> 
     */
    public void dragEnter(DropTargetDragEvent dtde) {}

    /**
     * Called when a drag operation is ongoing, while the mouse pointer is still
     * over the operable part of the drop site for the <code>DropTarget</code>
     * registered with this listener.
     * 
     * @param dtde the <code>DropTargetDragEvent</code> 
     */
    public void dragOver(DropTargetDragEvent dtde) {
    }

    /**
     * Called if the user has modified 
     * the current drop gesture.
     * <P>
     * @param dtde the <code>DropTargetDragEvent</code>
     */
    public void dropActionChanged(DropTargetDragEvent dtde) {}

    /**
     * Called while a drag operation is ongoing, when the mouse pointer has
     * exited the operable part of the drop site for the
     * <code>DropTarget</code> registered with this listener.
     * 
     * @param dte the <code>DropTargetEvent</code> 
     */
    public void dragExit(DropTargetEvent dte) {}

    /**
     * Called when the drag operation has terminated with a drop on
     * the operable part of the drop site for the <code>DropTarget</code>
     * registered with this listener.  
     * <p>
     * This method is responsible for undertaking
     * the transfer of the data associated with the
     * gesture. The <code>DropTargetDropEvent</code> 
     * provides a means to obtain a <code>Transferable</code>
     * object that represents the data object(s) to 
     * be transfered.<P>
     * From this method, the <code>DropTargetListener</code>
     * shall accept or reject the drop via the   
     * acceptDrop(int dropAction) or rejectDrop() methods of the 
     * <code>DropTargetDropEvent</code> parameter.
     * <P>
     * Subsequent to acceptDrop(), but not before,
     * <code>DropTargetDropEvent</code>'s getTransferable()
     * method may be invoked, and data transfer may be 
     * performed via the returned <code>Transferable</code>'s 
     * getTransferData() method.
     * <P>
     * At the completion of a drop, an implementation
     * of this method is required to signal the success/failure
     * of the drop by passing an appropriate
     * <code>boolean</code> to the <code>DropTargetDropEvent</code>'s
     * dropComplete(boolean success) method.
     * <P>
     * Note: The data transfer should be completed before the call  to the
     * <code>DropTargetDropEvent</code>'s dropComplete(boolean success) method.
     * After that, a call to the getTransferData() method of the
     * <code>Transferable</code> returned by
     * <code>DropTargetDropEvent.getTransferable()</code> is guaranteed to
     * succeed only if the data transfer is local; that is, only if
     * <code>DropTargetDropEvent.isLocalTransfer()</code> returns
     * <code>true</code>. Otherwise, the behavior of the call is
     * implementation-dependent.
     * <P>
     * @param dtde the <code>DropTargetDropEvent</code> 
     */
    public void drop(DropTargetDropEvent dtde) {}

    public void insertUpdate(DocumentEvent e) {
        model.setModified(true);
    }

    public void removeUpdate(DocumentEvent e) {
        model.setModified(true);
    }

    public void changedUpdate(DocumentEvent e) {
        model.setModified(true);
    }

    public void caretUpdate(CaretEvent e) {}

    public void stateChanged(ChangeEvent e) {}

    public void focusGained(FocusEvent e) {}

    public void focusLost(FocusEvent e) {}
    
    public class ControllerAction
        extends EasyAction
    {
        public ControllerAction() {
            this(null);
        }
        
        public ControllerAction(String name) {
            super(name);
        }
        
        public void actionPerformed(ActionEvent e) {
            Controller.this.actionPerformed(e);
        }
    }
}
