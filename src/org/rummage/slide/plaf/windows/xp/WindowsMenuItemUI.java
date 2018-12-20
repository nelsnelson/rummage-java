/*
 * BasicPopupMenuSeparatorUI.java
 *
 * Created on March 23, 2006, 11:51 AM
 */

package org.rummage.slide.plaf.windows.xp;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 * A Basic L&F implementation of PopupMenuSeparatorUI.  This implementation
 * is a "combined" view/controller.
 *
 * @author not attributable
 */
public class WindowsMenuItemUI
    extends com.sun.java.swing.plaf.windows.WindowsMenuItemUI
{
    public static ComponentUI createUI( JComponent c )
    {
        return new WindowsMenuItemUI();
    }

    public Dimension getPreferredSize( JComponent c )
    {
        Dimension preferredSize = super.getPreferredSize(c);
        
        preferredSize.height = preferredSize.height - 2;
        
        return preferredSize;
    }

}
