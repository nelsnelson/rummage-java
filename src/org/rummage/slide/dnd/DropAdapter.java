/*
 * DragAdapter.java
 *
 * Created on November 16, 2005, 5:00 PM
 */

package org.rummage.slide.dnd;

import java.awt.Color;
import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.border.Border;

/**
 *
 * @author nelsnelson
 */
public class DropAdapter
    implements DropTargetListener
{
    private Border oldBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
    
    private Border draggingOverRedBorder =
        BorderFactory.createLineBorder(Color.RED, 3);
    
    private Border draggingOverGreenBorder =
        BorderFactory.createLineBorder(Color.GREEN.brighter(), 3);
    
    private Border draggingOverBorder = draggingOverRedBorder;

    private int actions;

    private Component dropTarget = null;
    
    /**
     * Called while a drag operation is ongoing, when the mouse pointer enters
     * the operable part of the drop site for the <code>DropTarget</code>
     * registered with this listener. 
     * 
     * @param dtde the <code>DropTargetDragEvent</code> 
     */
    public void dragEnter(DropTargetDragEvent dtde) {
        Object o = dtde.getDropTargetContext().getComponent();
        
        if (o instanceof JComponent) {
            JComponent target = (JComponent) o;
            
            TransferHandler transferHandler = target.getTransferHandler();
            
            if (transferHandler != null) {
                if (transferHandler.canImport(target,
                    dtde.getCurrentDataFlavors()))
                {
                    draggingOverBorder = draggingOverGreenBorder;
                }
                else {
                    draggingOverBorder = draggingOverRedBorder;
                }
            }
            
            oldBorder = target.getBorder();
            target.setBorder(draggingOverBorder);
        }
    }

    /**
     * Called when a drag operation is ongoing, while the mouse pointer is still
     * over the operable part of the drop site for the <code>DropTarget</code>
     * registered with this listener.
     * 
     * @param dtde the <code>DropTargetDragEvent</code> 
     */
    public void dragOver(DropTargetDragEvent dtde) {
        Object o = dtde.getDropTargetContext().getComponent();
        
        if (o instanceof DragAndDropable) {
            DragAndDropable target = (DragAndDropable) o;
            
            target.setDraggingOver(dtde.getLocation());
        }
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
    public void dragExit(DropTargetEvent dte) {
        Object o = dte.getDropTargetContext().getComponent();
        
        if (o instanceof JComponent) {
            JComponent target = (JComponent) o;
            
            target.setBorder(oldBorder);
        }
        
        if (o instanceof DragAndDropable) {
            DragAndDropable target = (DragAndDropable) o;
            
            target.setDraggingOver(null);
        }
    }

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
    public void drop(DropTargetDropEvent dtde) {
        Object o = dtde.getDropTargetContext().getComponent();
        
        if (o instanceof JComponent) {
            JComponent target = (JComponent) o;
            
            target.setBorder(oldBorder);
            
            TransferHandler transferHandler = target.getTransferHandler();
            
            if (transferHandler != null) {
                Transferable t = dtde.getTransferable();
                
                //transferHandler.importData(target, dtde.getTransferable());
            }
        }
        else if (o instanceof DragAndDropable) {
            DragAndDropable target = (DragAndDropable) o;
            
            Transferable t = dtde.getTransferable();
            
            try {
                target.dropped(t.getTransferData(t.
                    getTransferDataFlavors()[0]), dtde.getLocation());
            }
            catch (UnsupportedFlavorException ex) {
                ex.printStackTrace(System.out);
                return;
            }
            catch (IOException ex) {
                ex.printStackTrace(System.out);
                return;
            }
            
            target.setDraggingOver(null);
        }
    }
}
