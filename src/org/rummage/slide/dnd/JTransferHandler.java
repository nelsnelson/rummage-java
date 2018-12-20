/*
 * DragAndDropTransferHandler.java
 *
 * Created on March 1, 2006, 12:33 PM
 */

package org.rummage.slide.dnd;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.rummage.slide.ColorUtilities;

/**
 *
 * @author not attributable
 */
public class JTransferHandler
    extends TransferHandler
    implements DropTargetListener
{
    private Border oldBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
    
    private Border draggingOverRedBorder =
        BorderFactory.createLineBorder(ColorUtilities.getTranslucent(Color.
        RED, 0.5d), 3);
    
    private Border draggingOverGreenBorder =
        BorderFactory.createLineBorder(ColorUtilities.getTranslucent(Color.
        GREEN.brighter(), 0.5d), 3);
    
    private Border draggingOverBorder = draggingOverRedBorder;
    private Insets draggingOverMargin = new Insets(0, 0, 0, 0);
    
    private DropHandler dropHandler = null;
    private DropTarget dropTarget = null;
    private TransferDelegate transferDelegate = null;
    private TransferHandler originalTransferHandler = null;
    
    public JTransferHandler() {
        this(null, null);
    }
    
    public JTransferHandler(JComponent comp) {
        this(comp, null);
    }
    
    public JTransferHandler(JComponent comp, TransferDelegate delegate) {
        setDropTarget(comp);
        setTransferDelegate(delegate);
        setOriginalTransferHandler(comp.getTransferHandler());
    }
    
    public void setDropTarget(JComponent comp) {
        if (comp == null) {
            return;
        }
        
        dropTarget = new DropTarget(comp, getDropHandler());
        //dropTarget = new DropTarget();
        
        //comp.setDropTarget(dropTarget);
        
        //try {
        //    dropTarget.addDropTargetListener(getDropHandler());
        //}
        //catch (TooManyListenersException ex) {
        //    ex.printStackTrace(System.out);
        //}
    }
    
    private DropTargetListener getDropHandler() {
        dropHandler = new DropHandler(this);
        
        return dropHandler;
    }
    
    public void setTransferDelegate(TransferDelegate delegate) {
        this.transferDelegate = delegate;
    }
    
    public void setOriginalTransferHandler(TransferHandler transferHandler) {
        this.originalTransferHandler = transferHandler;
    }
    
    public TransferHandler getOriginalTransferHandler() {
        return originalTransferHandler;
    }
    
    private class DropHandler implements DropTargetListener, Serializable {
        
        private boolean canImport;
        
        private DropTargetListener proxy = null;
        
        public DropHandler(DropTargetListener proxy) {
            this.proxy = proxy;
        }
        
        private boolean actionSupported(int action) {
            return (action & (COPY_OR_MOVE | DnDConstants.ACTION_LINK)) != NONE;
        }

        // --- DropTargetListener methods -----------------------------------

        public void dragEnter(DropTargetDragEvent e) {
            proxy.dragEnter(e);
            
            DataFlavor[] flavors = e.getCurrentDataFlavors();

            JComponent c = (JComponent)e.getDropTargetContext().getComponent();
            TransferHandler importer = c.getTransferHandler();
            
            if (importer != null && importer.canImport(c, flavors)) {
                canImport = true;
            } else {
                canImport = false;
            }
            
            int dropAction = e.getDropAction();
            
            if (canImport && actionSupported(dropAction)) {
                e.acceptDrag(dropAction);
            } else {
                e.rejectDrag();
            }
        }

        public void dragOver(DropTargetDragEvent e) {
            proxy.dragOver(e);
            
            int dropAction = e.getDropAction();
            
            if (canImport && actionSupported(dropAction)) {
                e.acceptDrag(dropAction);
            } else {
                e.rejectDrag();
            }
        }

        public void dragExit(DropTargetEvent e) {
            proxy.dragExit(e);
        }

        public void drop(DropTargetDropEvent e) {
            proxy.drop(e);
            
            int dropAction = e.getDropAction();

            JComponent c = (JComponent)e.getDropTargetContext().getComponent();
            TransferHandler importer = c.getTransferHandler();

            if (canImport && importer != null && actionSupported(dropAction)) {
                e.acceptDrop(dropAction);
                
                try {
                    Transferable t = e.getTransferable();
                    e.dropComplete(importer.importData(c, t));
                } catch (RuntimeException re) {
                    e.dropComplete(false);
                }
            } else {
                e.rejectDrop();
            }
        }

        public void dropActionChanged(DropTargetDragEvent e) {
            proxy.dropActionChanged(e);
            
            int dropAction = e.getDropAction();
            
            if (canImport && actionSupported(dropAction)) {
                e.acceptDrag(dropAction);
            } else {
                e.rejectDrag();
            }
        }
    }
    
    /**
     * Need to provide the normal copy stuff since this might replace the
     * <code>DefaultTransferHandler</code> of a <code>JTextComponent</code>
     * and plain old <code>TransferHandler</code> isn't up to the task.
     */
    public void exportToClipboard(JComponent comp, Clipboard clipboard,
                                  int action) throws IllegalStateException {
        if (comp instanceof JTextComponent) {
            JTextComponent text = (JTextComponent)comp;
            int p0 = text.getSelectionStart();
            int p1 = text.getSelectionEnd();
            if (p0 != p1) {
                try {
                    Document doc = text.getDocument();
                    String srcData = doc.getText(p0, p1 - p0);
                    StringSelection contents =new StringSelection(srcData);

                    // this may throw an IllegalStateException,
                    // but it will be caught and handled in the
                    // action that invoked this method
                    clipboard.setContents(contents, null);

                    if (action == TransferHandler.MOVE) {
                        doc.remove(p0, p1 - p0);
                    }
                } catch (BadLocationException ble) {}
            }
        }
    }

    public boolean importData(JComponent comp, Transferable t) {
        try {
            if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                Object data = t.getTransferData(DataFlavor.stringFlavor);
                
                if (data == null) {
                    return false;
                }
                
                return
                    handleTransfer(data) ? true :
                        getOriginalTransferHandler().importData(comp, t);
            }
            else if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                Object data = t.getTransferData(DataFlavor.javaFileListFlavor);
                
                if (data == null) {
                    return false;
                }
                
                return handleTransfer(data);
            }
        }
        catch (UnsupportedFlavorException e) {
            return false;
        }
        catch (IOException e) {
            return false;
        }
        
        return
            super.importData(comp, t) ? true :
                getOriginalTransferHandler().importData(comp, t);
    }

    public boolean canImport(JComponent component, DataFlavor[] flavors) {
        // To do: Filter transferable data with a custom and overridable
        // isDataFlavorSupported() method that by default will verify that
        // data is a file or the name of a file of a type supported by the
        // overriding application without inadvertantly filtering out
        // regularly accepted flavors.

        return
            super.canImport(component, flavors) ||
            (flavors.length > 0 && flavors[0].equals(DataFlavor.stringFlavor)) ||
            (flavors.length > 0 && flavors[0].equals(DataFlavor.javaFileListFlavor));
    }
    
    public boolean handleTransfer(Object data) {
        if (transferDelegate == null) {
            return false;
        }
        
        return transferDelegate.handleTransfer(data);
    }
    
    /**
     * Called while a drag operation is ongoing, when the mouse pointer enters
     * the operable part of the drop site for the <code>DropTarget</code>
     * registered with this listener. 
     * 
     * @param dtde the <code>DropTargetDragEvent</code> 
     */
    public void dragEnter(DropTargetDragEvent dtde) {
        alterBorder(dtde.getDropTargetContext().getComponent(), dtde);
    }

    public void dragOver(DropTargetDragEvent dtde) {
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    public void dragExit(DropTargetEvent dte) {
        resetBorder(dte.getDropTargetContext().getComponent());
    }

    public void drop(DropTargetDropEvent dtde) {
        Component c = dtde.getDropTargetContext().getComponent();
        
        resetBorder(c);
        
        SwingUtilities.getWindowAncestor(c).toFront();
    }
    
    private void alterBorder(Object o, DropTargetDragEvent dtde) {
        if (o instanceof JComponent) {
            JComponent target = (JComponent) o;
            
            if (canImport(target, dtde.getCurrentDataFlavors())) {
                draggingOverBorder = draggingOverGreenBorder;
            }
            else {
                draggingOverBorder = draggingOverRedBorder;
            }
            
            oldBorder = target.getBorder();
            
            target.setBorder(BorderFactory.
                createCompoundBorder(draggingOverBorder, oldBorder));
        }
    }
    
    private void resetBorder(Object o) {
        if (o instanceof JComponent) {
            JComponent target = (JComponent) o;
            
            target.setBorder(oldBorder);
        }
    }
}
