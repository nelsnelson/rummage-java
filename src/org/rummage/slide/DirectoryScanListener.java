/*
 * DirectoryScanListener.java
 *
 * Created on June 28, 2006, 4:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.rummage.slide;

import java.util.EventListener;

import org.rummage.slide.event.DirectoryScanEvent;

/**
 *
 * @author nelsnelson
 */
public interface DirectoryScanListener
    extends EventListener
{
    public void directoryScan(DirectoryScanEvent e);
}
