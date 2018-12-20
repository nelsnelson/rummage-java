package org.rummage.slide;

import org.rummage.slide.plaf.windows.xp.CloseableTabbedPaneUI;

public class JTabbedPane
    extends javax.swing.JTabbedPane
{
    public JTabbedPane() {
        this(JTabbedPane.TOP);
    }
    
    public JTabbedPane(int tabPlacement) {
        super(tabPlacement);
        
        setUI(new CloseableTabbedPaneUI(this));
    }
}
