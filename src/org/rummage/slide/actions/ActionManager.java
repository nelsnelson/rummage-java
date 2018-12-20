/*
 * ActionManager.java
 *
 * Created on May 11, 2006, 3:31 PM
 */

package org.rummage.slide.actions;

import java.awt.Component;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.swing.Action;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 *
 * @author nelsnelson
 */
public class ActionManager
    implements ActionListener, AncestorListener, CaretListener,
    ChangeListener, ContainerListener, DocumentListener, DropTargetListener,
    FocusListener, KeyListener, ListSelectionListener, MouseListener,
    MouseMotionListener, TreeSelectionListener
{
    private Map<Component, Action> m = null;
    
    /** Creates a new instance of ActionManager */
    public ActionManager() {
         m = new WeakHashMap();
    }
    
    public void register(Component c, Action a) {
        m.put(c, a);
    }
    
    public void unregister(Component c) {
        m.remove(c);
    }
    
    public void update() {
        Set<Component> components = m.keySet();
        
        for (Component c : components) {
            c.setEnabled(m.get(c).isEnabled());
        }
    }
    
    public void listenToSubTree(java.awt.Container parent) {
        java.awt.Component[] children = parent.getComponents();
        
        for (java.awt.Component child : children) {
            if (child instanceof javax.swing.JComponent) {
                javax.swing.JComponent c = (javax.swing.JComponent) child;
                
                if (!java.util.Arrays.asList(c.getContainerListeners()).
                    contains(this))
                {
                    c.addContainerListener(this);
                }
            }
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        update();
    }
    
    public void componentAdded(ContainerEvent e) {
        java.awt.Component c = e.getChild();
        
        if (c instanceof java.awt.Container) {
            listenToSubTree((java.awt.Container) c);
        }
        
        if (c instanceof javax.swing.JButton) {
            javax.swing.JButton b = (javax.swing.JButton) c;
            
            if (!m.containsKey(b)) {
                m.put(b, b.getAction());
            }
        }
        else if (c instanceof javax.swing.JCheckBox) {
            javax.swing.JCheckBox b = (javax.swing.JCheckBox) c;
            
            if (!m.containsKey(b)) {
                m.put(b, b.getAction());
            }
        }
        else if (c instanceof javax.swing.JRadioButton) {
            javax.swing.JRadioButton b = (javax.swing.JRadioButton) c;
            
            if (!m.containsKey(b)) {
                m.put(b, b.getAction());
            }
        }
        else if (c instanceof javax.swing.JMenuItem) {
            javax.swing.JMenuItem b = (javax.swing.JMenuItem) c;
            
            if (!m.containsKey(b)) {
                m.put(b, b.getAction());
            }
        }
        else if (c instanceof javax.swing.JCheckBoxMenuItem) {
            javax.swing.JCheckBoxMenuItem b = (javax.swing.JCheckBoxMenuItem) c;
            
            if (!m.containsKey(b)) {
                m.put(b, b.getAction());
            }
        }
        else if (c instanceof javax.swing.JRadioButtonMenuItem) {
            javax.swing.JRadioButtonMenuItem b = (javax.swing.JRadioButtonMenuItem) c;
            
            if (!m.containsKey(b)) {
                m.put(b, b.getAction());
            }
        }
    }

    public void componentRemoved(ContainerEvent e) {
        java.awt.Component c = e.getChild();
        
        if (m.containsKey(c)) {
            m.remove(c);
        }
    }

    public void caretUpdate(CaretEvent e) {
        update();
    }

    public void stateChanged(ChangeEvent e) {
        update();
    }

    public void insertUpdate(DocumentEvent e) {
        update();
    }

    public void removeUpdate(DocumentEvent e) {
        update();
    }

    public void changedUpdate(DocumentEvent e) {
        update();
    }

    public void dragEnter(DropTargetDragEvent dtde) {
        update();
    }

    public void dragOver(DropTargetDragEvent dtde) {
        update();
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
        update();
    }

    public void dragExit(DropTargetEvent dte) {
        update();
    }

    public void drop(DropTargetDropEvent dtde) {
        update();
    }

    public void focusGained(FocusEvent e) {
        update();
    }

    public void focusLost(FocusEvent e) {
        update();
    }

    public void keyTyped(KeyEvent e) {
        update();
    }

    public void keyPressed(KeyEvent e) {
        update();
    }

    public void keyReleased(KeyEvent e) {
        update();
    }

    public void valueChanged(ListSelectionEvent e) {
        update();
    }

    public void mouseClicked(MouseEvent e) {
        update();
    }

    public void mousePressed(MouseEvent e) {
        update();
    }

    public void mouseReleased(MouseEvent e) {
        update();
    }

    public void mouseEntered(MouseEvent e) {
        update();
    }

    public void mouseExited(MouseEvent e) {
        update();
    }

    public void mouseDragged(MouseEvent e) {
        update();
    }

    public void mouseMoved(MouseEvent e) {
        update();
    }

    public void valueChanged(TreeSelectionEvent e) {
        update();
    }

    public void ancestorAdded(AncestorEvent event) {
    }

    public void ancestorRemoved(AncestorEvent event) {
    }

    public void ancestorMoved(AncestorEvent event) {
    }
}
