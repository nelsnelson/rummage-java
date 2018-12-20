/*
 * TransparentBorder.java
 *
 * Created on October 21, 2005, 3:36 PM
 */

package org.rummage.slide.border;

import javax.swing.border.AbstractBorder;

/**
 *
 * @author nelsnelson
 */
public class TransparentBorder
    extends AbstractBorder
{
    public boolean isBorderOpaque() {
        return false;
    }
}
