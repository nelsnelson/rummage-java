/*
 * CornerPeer.java
 *
 * Created on March 23, 2006, 8:45 AM
 */

package org.rummage.slide;

import java.awt.Component;

/**
 *
 * @author nelsnelson
 */
public interface CornerPeer {
    public void setCornerComponent(Component corner);
    
    public boolean isVisible();
}
