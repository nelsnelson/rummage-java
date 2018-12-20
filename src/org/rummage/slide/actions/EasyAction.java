/*
 * EasyAction.java
 *
 * Created on May 2, 2006, 8:21 AM
 */
package org.rummage.slide.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

/**
 *
 * @author nnelson
 */
public class EasyAction
    extends AbstractAction
{
    /** Creates a new instance of EasyAction */
    public EasyAction(String name) {
        super(name);
    }
    
    /** Creates a new instance of EasyAction */
    public EasyAction(String name, char mnemonic) {
        super(name);
        
        putValue(MNEMONIC_KEY, mnemonic);
    }
    
    /** Creates a new instance of EasyAction */
    public EasyAction(String name, char mnemonic, String accelerator) {
        super(name);
        
        putValue(MNEMONIC_KEY, (int) mnemonic);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
    }

    public void actionPerformed(ActionEvent e) {
    }
}
