/*
 * CloseableTabbedPaneUI.java
 *
 * Created on August 9, 2006, 2:32 PM
 */

package org.rummage.slide.plaf.windows.xp;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import org.rummage.slide.JButton;
import org.rummage.slide.JTabbedPane;
import org.rummage.slide.SlideUtilities;
import org.rummage.toolbox.util.UIManager;

/**
 *
 * @author nelsnelson
 */
public class CloseableTabbedPaneUI
    //extends com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI
    extends javax.swing.plaf.basic.BasicTabbedPaneUI
{
    private static final String ICON_1 =
        "images/Windows_XP_Small_Window_Close_Icon.png";
    
    private static final String ICON_2 =
        "images/Windows_XP_Small_Window_Close_Mouse_Clicked_Icon.png";
    
    private static final String ICON_3 =
        "images/Windows_XP_Small_Window_Close_Mouse_Over_Icon.png";
    
    private static final URL NORMAL = JTabbedPane.class.getResource(ICON_1);
    private static final URL CLICKED = JTabbedPane.class.getResource(ICON_2);
    private static final URL HOVERING = JTabbedPane.class.getResource(ICON_3);
    
    private JTabbedPane tabPane = null;
    private CloseableTabbedPaneLayout layout = null;
    
    //a list of close buttons
    public List<JButton> closeButtons = new ArrayList();
    
    /** Creates a new instance of CloseableTabbedPaneUI */
    public CloseableTabbedPaneUI(JTabbedPane tabPane) {
        this.tabPane = tabPane;
        
        tabInsets = UIManager.getInsets("TabbedPane.tabInsets");
    }
    
    public LayoutManager createLayoutManager() {
        if (layout == null) {
            layout = new CloseableTabbedPaneLayout();
        }
        
        return layout;
    }
    
    private int getTitleWidth(int i) {
        FontMetrics fm = SlideUtilities.getFontMetrics(tabPane.getGraphics());
        
        return fm.stringWidth(tabPane.getTitleAt(i));
    }
    
    protected Insets getTabInsets(int tabPlacement,int tabIndex) {
        Insets defaultInsets = (Insets) super.getTabInsets(tabPlacement, tabIndex).clone();
        
        if (tabIndex < closeButtons.size()) {
            JButton closeButton = closeButtons.get(tabIndex);
            defaultInsets.right += closeButton.getPreferredSize().width + 20;
            defaultInsets.left = 2;
        }
        
        return defaultInsets;
    } 

    /**
     * This is set to true when a component is added/removed from the tab
     * pane and set to false when layout happens.  If true it indicates that
     * tabRuns is not valid and shouldn't be used.
     */
    private boolean isRunsDirty;

    /**
     * Repaints the specified tab.
     */
    private void repaintTab(int index) {
        // If we're not valid that means we will shortly be validated and
        // painted, which means we don't have to do anything here.
        if (!isRunsDirty && index >= 0 && index < tabPane.getTabCount()) {
            tabPane.repaint(getTabBounds(tabPane, index));
        }
    }
    
    class CloseableTabbedPaneLayout
        extends TabbedPaneLayout
    {
        public CloseableTabbedPaneLayout() {
            super();
        }
        
        public void layoutContainer(Container parent) {
            super.layoutContainer(parent);
            //ensure that there are at least as many close buttons as tabs
            while(tabPane.getTabCount() > closeButtons.size()) {
                closeButtons.add(new JTabCloseButton(closeButtons.size()));
            }
            Rectangle rect = new Rectangle();
            int i;
            for(i = 0; i < tabPane.getTabCount(); i++) {
                rect = getTabBounds(i, rect);
                JButton closeButton = (JButton)closeButtons.get(i);
                Dimension d = closeButton.getPreferredSize();
                
                int x = rect.x+rect.width-d.width-5;
                int y = rect.y+rect.height-d.height-4;
                
                if (tabPane.getSelectedIndex() == i) {
                    x = x - 1;
                    y = y - 2;
                }
                
                closeButton.setSize(d);
                closeButton.setLocation(x, y);
                
                tabPane.add(closeButton);
            }
            for(;i < closeButtons.size(); i++) {
                //remove any extra close buttons
                tabPane.remove((JButton)closeButtons.get(i));
            }
        }

        // implement UIResource so that when we add this button to the
        // tabbedpane, it doesn't try to make a tab for it!
        class JTabCloseButton
            extends JButton
            implements javax.swing.plaf.UIResource
        {
            public JTabCloseButton(final int index) {
                super(new TabCloseButtonAction(index));
                setToolTipText("Close this tab");
                setBorder(BorderFactory.createEmptyBorder());
                
                setIcon(new ImageIcon(NORMAL));
                setPressedIcon(new ImageIcon(CLICKED));
                setRolloverIcon(new ImageIcon(HOVERING));
                
                Dimension d =
                    new Dimension(getIcon().getIconWidth(),
                    getIcon().getIconHeight());
                
                setPreferredSize(d);
                setMaximumSize(d);
                setMinimumSize(d);
                
                //remove the typical padding for the button
                setMargin(new Insets(0,0,0,0));
                
                // When the cursor is <i>anywhere</i> inside the rectangle of
                // the tab, the expected behavior is that the tab will paint
                // its hilighting.  Without the following, a tab would refuse
                // to hilight even though the cursor was hovering within its
                // bounds because the mouseEntered event gets caught by the
                // special tab close button, instead of the tab.
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        setRolloverTab(index); // only in 1.5
                        repaintTab(index);
                    }
                });
            }
        }
        
        class TabCloseButtonAction
            extends AbstractAction
        {
            // This could be done away with if this class were anonymously
            // instantiated inside the JTabCloseButton class, but this is the
            // way I wanna do it.
            int index;
            
            public TabCloseButtonAction(int index) {
                super();
                this.index = index;
            }
            
            public void actionPerformed(ActionEvent e) {
                if (index < closeButtons.size()) {
                    JButton b = closeButtons.get(index);
                    for (MouseListener l : b.getMouseListeners()) {
                        // Artificially reset the close button so that it
                        // doesn't look strange when another tab has been
                        // closed.
                        l.mouseExited(new MouseEvent(b,
                            MouseEvent.MOUSE_EXITED, e.getWhen(),
                            e.getModifiers(), 0, 0, 0, false));
                    }
                }
                tabPane.remove(index);
            }
        }
    }
}
