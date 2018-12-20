/*
 * ComponentSelectionListener.java
 *
 * Created on June 27, 2006, 4:26 PM
 */

package org.rummage.slide;

import java.util.EventListener;

import org.rummage.slide.event.ComponentSelectionEvent;

/**
 *
 * @author nnelson
 */
public interface ComponentSelectionListener
    extends EventListener
{
    public void componentSelected(ComponentSelectionEvent e);
}
