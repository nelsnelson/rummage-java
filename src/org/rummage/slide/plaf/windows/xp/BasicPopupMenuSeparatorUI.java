/*
 * BasicPopupMenuSeparatorUI.java
 *
 * Created on March 23, 2006, 11:51 AM
 */

package org.rummage.slide.plaf.windows.xp;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;

/**
 * A Basic L&F implementation of PopupMenuSeparatorUI.  This implementation
 * is a "combined" view/controller.
 *
 * @author not attributable
 */
public class BasicPopupMenuSeparatorUI extends BasicSeparatorUI
{
    public static ComponentUI createUI( JComponent c )
    {
        return new BasicPopupMenuSeparatorUI();
    }

    public void paint( Graphics g, JComponent c )
    {
        Dimension s = c.getSize();
	
        g.setColor( c.getForeground() );
        g.drawLine( 1, 3, s.width-1, 3 );
        
        g.setColor( c.getBackground() );
        g.drawLine( 1, 4, s.width-1, 4 );
    }

    public Dimension getPreferredSize( JComponent c )
    {
        return new Dimension( 0, 9 );
    }

}
