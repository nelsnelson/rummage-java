/*
 * JMenuBar.java
 *
 * Created on May 18, 2006, 2:07 PM
 */

package org.rummage.slide;

/**
 *
 * @author nnelson
 */
public class JMenuBar 
    extends javax.swing.JMenuBar
{
    /** Creates a new instance of JMenuBar */
    public JMenuBar() {
        super();
    }

    public void insertMenuItem(String text, char mnemonic, String k,
        String menuName, String menuItemNameToFollow)
    {
        JMenu menu = SlideUtilities.getMenu(this, menuName);
        
        if (menu == null) {
            return;
        }
        
        int index =
            SlideUtilities.getIndexOfMenuItemByName(menu, menuItemNameToFollow);
        
        menu.insert(text, mnemonic, k, SlideUtilities.
            getIndexOfMenuItemByName(menu, menuItemNameToFollow));
    }
}
