package org.rummage.slide;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

public class AbstractView
    extends JFrame
    implements Observer, View<Object>, WindowListener, ComponentListener
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void componentResized(ComponentEvent e) {
        return;
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        return;
    }

    @Override
    public void componentShown(ComponentEvent e) {
        return;
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        return;
    }

    @Override
    public void windowOpened(WindowEvent e) {
        return;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        return;
    }

    @Override
    public void windowClosed(WindowEvent e) {
        return;
    }

    @Override
    public void windowIconified(WindowEvent e) {
        return;
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        return;
    }

    @Override
    public void windowActivated(WindowEvent e) {
        return;
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        return;
    }

    @Override
    public void showAboutDialog() {
        return;
    }

    @Override
    public void update(Observable o, Object arg) {
        return;
    }

    @Override
    public JToolBar getToolBar() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JStatusBar getStatusBar() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void windowClosing(Object o) {
        return;
    }

    @Override
    public String getApplicationTitleString() {
        return null;
    }
}
