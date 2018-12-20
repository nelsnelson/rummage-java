/*
 * FocusAgent.java
 *
 * Created on June 1, 2006, 4:31 PM
 */

package org.rummage.slide;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.text.JTextComponent;

/**
 *
 * @author nnelson
 */
public class FocusAgent
    extends ComponentAdapter
{
    private JTextComponent tc = null;
    
    /** Creates a new instance of FocusAgent */
    public FocusAgent(JTextComponent tc) {
        this.tc = tc;
    }
    
    public void componentShown(ComponentEvent e) {
        tc.selectAll();
        tc.requestFocus();
    }
    
    public void componentHidden(ComponentEvent e) {
        e.getComponent().requestFocus();
        //tc.dispatchEvent(new FocusEvent(tc, FocusEvent.FOCUS_LOST));
    }
}
