/*
 * DoubleClickActionAdapter.java
 *
 * Created on June 1, 2006, 5:21 PM
 */

package org.rummage.slide;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;

/**
 *
 * @author nelsnelson
 */
public class DoubleClickAdapter
    extends MouseAdapter
    implements ActionListener
{
    private Action a;
    
    /** Creates a new instance of DoubleClickActionAdapter */
    public DoubleClickAdapter(String text, final ActionListener listener) {
        a = new AbstractAction(text) {
            public void actionPerformed(ActionEvent e) {
                listener.actionPerformed(e);
            }
        };
    }
    
    public DoubleClickAdapter() {
        
    }
    
    public DoubleClickAdapter(Action a) {
        this.a = a;
    }
    
    public DoubleClickAdapter(JComponent c) {
        c.addMouseListener(this);
    }
    
    public DoubleClickAdapter(JComponent c, String text, ActionListener listener) {
        this(text, listener);
        
        c.addMouseListener(this);
    }
    
    public DoubleClickAdapter(JComponent c, Action a) {
        this(a);
        
        c.addMouseListener(this);
    }
    
    public void mouseDoubleClicked(MouseEvent me) {
        
    }
    
    public void mouseClicked(MouseEvent me) {
        if (me.getClickCount() == 2) {
            if (a == null) {
                actionPerformed(new ActionEvent(me.getSource(),
                    ActionEvent.ACTION_PERFORMED, "doubleClick"));
            }
            else {
                a.actionPerformed(new ActionEvent(me.getSource(),
                    ActionEvent.ACTION_PERFORMED,
                    (String) a.getValue(Action.NAME)));
            }
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        mouseDoubleClicked(null);
    }
}
