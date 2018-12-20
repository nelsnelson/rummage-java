/*
 * View.java
 *
 * Created on February 3, 2006, 12:56 PM
 */
package org.rummage.slide;

/**
 *
 * @author nelsnelson
 */
public interface View<T> {
    public void setCursor(java.awt.Cursor cursor);
    
    public javax.swing.JMenuBar getJMenuBar();
    
    public org.rummage.slide.JToolBar getToolBar();
    
    public org.rummage.slide.JStatusBar getStatusBar();
    
    public void dispose();
    
    public void showAboutDialog();
    
    public void windowClosing(Object o);
    
    public String getApplicationTitleString();
}
