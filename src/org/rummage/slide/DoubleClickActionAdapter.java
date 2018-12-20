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

/**
 *
 * @author nnelson
 */
public class DoubleClickActionAdapter
    extends MouseAdapter
{
    private Action a;
    
    /** Creates a new instance of DoubleClickActionAdapter */
    public DoubleClickActionAdapter(String text, final ActionListener listener) {
        a = new AbstractAction(text) {
            public void actionPerformed(ActionEvent e) {
                listener.actionPerformed(e);
            }
        };
    }
    
    public DoubleClickActionAdapter(Action a) {
        this.a = a;
    }
    
    public void mouseClicked(MouseEvent me) {
        if (me.getClickCount() == 2) {
            a.actionPerformed(new ActionEvent(me.getSource(),
                ActionEvent.ACTION_PERFORMED,
                (String) a.getValue(Action.NAME)));
        }
    }
}
