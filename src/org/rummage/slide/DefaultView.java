/*
 * DefaultView.java
 *
 * Created on October 19, 2005, 10:03 AM
 */

package org.rummage.slide;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.rummage.slide.context.ContextManager;
import org.rummage.toolbox.util.Properties;

/**
 *
 * @author nelsnelson
 */
public class DefaultView
    extends AbstractView
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected static Controller<?> controller = null;
    protected Model model = null;

    public static final boolean DEMO = false, DEBUG = false;
    
    protected JEditMenu editMenu = null;
    protected JMenuBar menuBar = null;
    protected JToolBar toolBar = null;
    protected JStatusBar statusBar = null;
    
    private Dimension preferredDimension = null;
    
    /**
     * Creates a new instance of DefaultView
     */
    public DefaultView() {
        this(null);
    }
    
    /**
     * Creates a new instance of DefaultView
     */
    public DefaultView(Controller<?> controller) {
        DefaultView.controller = controller;
        DefaultView.controller.view = this;
        
        new ContextManager();
        
        addComponentListener(this);
        getContentPane().addContainerListener(controller);
        addWindowListener(this);
        
        initUI();
        
        toolBar.setVisible(Properties.getBoolean(Properties.
            TOOL_BAR_VISIBILITY));
        statusBar.setVisible(Properties.getBoolean(Properties.
            STATUS_BAR_VISIBILITY));
        
        setVisible(true);
    }
    
    public Observer getObserverView() {
        return (Observer) this;
    }
    
    public void update(Observable o, Object arg) {
        
    }
    
    /**
     * Calling this will apparently force an immediate repaint, regardless of
     * the execution state or progress of other threads.  Be careful with
     * this -- it can cause unexpected results.  For instance, if called after
     * the execution of a <code>Callable</code>'s <code>call()</code> method,
     * it may seem as though the Callable thread has finished executing when
     * in fact it has not.  Basically, what is happening here is that the
     * <code>super.update(Graphics g)</code> method invocation causes an
     * AWTEvent to be queued and executed before any concurrently executing
     * threads.  H'rumph.
     */
    public void update() {
        super.update(getGraphics());
    }
    
    public synchronized void updateImmediately() {
        new Thread(new Runnable() {
            public void run() {
                update();
            }
        }).start();
    }
    
    public void toFront() {
        super.toFront();
        paintAll(getGraphics());
    }
    
    protected void initUI() {
        setTitle(getApplicationTitleString());
        
        initPositioning();

        menuBar = createMenuBar0();
        toolBar = createToolBar();
        statusBar = createStatusBar();
        
        setJMenuBar(menuBar);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(toolBar, BorderLayout.BEFORE_FIRST_LINE);
        contentPanel.add(initContent(), BorderLayout.CENTER);
        contentPanel.add(statusBar, BorderLayout.AFTER_LAST_LINE);
        getContentPane().add(contentPanel);
        //add(contentPanel);
        setLocationRelativeTo(null);
        
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
    
    public String getApplicationTitleString() {
        return Properties.getProperty(Properties.WINDOW_TITLE);
    }
    
    protected JPanel initContent() {
        return new JPanel();
    }
    
    protected JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar("Main", menuBar);
        
        JButton openButton = new JButton("Open");
        openButton.addActionListener(controller);
        toolBar.add(openButton);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(controller);
        toolBar.add(closeButton);
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(controller);
        toolBar.add(saveButton);
        
        return toolBar;
    }
    
    protected JStatusBar createStatusBar() {
        return new JStatusBar(this);
    }

    private final JMenuBar createMenuBar0() {
        JMenuBar menuBar = createMenuBar();
        
        List<String> menus = SlideUtilities.getMenuNames(menuBar);
        
        if (!menus.contains("File")) {
            // File menu
            JMenu fileMenu = new JMenu(this, "File", 'F');

            fileMenu.add("Open File...", 'O', "control O");

            fileMenu.add("Close", 'C');

            fileMenu.addSeparator();

            fileMenu.add("Exit", 'x');

            fileMenu.addActionListener(controller);
            menuBar.add(fileMenu);
        }

        if (!menus.contains("Edit")) {
            // Edit menu
            JEditMenu editMenu = new JEditMenu(this);
            menuBar.add(editMenu);
        }
        
        if (!menus.contains("View")) {
            // View menu
            JMenu viewMenu = new JMenu(this, "View", 'V');

            viewMenu.add("Tool Bar", 'T', Properties.getBoolean(Properties.
                TOOL_BAR_VISIBILITY));

            viewMenu.add("Status Bar", 'S', Properties.getBoolean(Properties.
                STATUS_BAR_VISIBILITY));

            viewMenu.addActionListener(controller);
            menuBar.add(viewMenu);
        }
        
        if (!menus.contains("Help")) {
            JMenu helpMenu = new JMenu(this, "Help", 'H');
            
            helpMenu.add("Help Topics", 'H');
            
            helpMenu.addSeparator();
            
            helpMenu.add("About " + getApplicationTitleString(), 'A');
            
            helpMenu.addActionListener(controller);
            menuBar.add(helpMenu);
        }

        return menuBar;
    }
    
    protected JMenuBar createMenuBar() {
        return new JMenuBar();
    }
    
    public JToolBar getToolBar() {
        return this.toolBar;
    }
    
    public JStatusBar getStatusBar() {
        return this.statusBar;
    }
    
    public JEditMenu getEditMenu() {
        return (JEditMenu) this.getMenu("Edit");
    }
    
    public JMenu getMenu(String menuName) {
        javax.swing.JMenuBar menuBar = this.getRootPane().getJMenuBar();
        return SlideUtilities.getMenu(menuBar, menuName);
    }
    
    protected Dimension getPreferredDimension() {
        return this.preferredDimension;
    }
    
    protected void initPositioning() {
        int extendedState =
            Properties.getInt(Properties.WINDOW_EXTENDED_STATE);
        
        if (extendedState == JFrame.MAXIMIZED_BOTH) {
            this.setExtendedState(extendedState);
        }
        
        int x = Properties.getInt(Properties.WINDOW_X);
        int y = Properties.getInt(Properties.WINDOW_Y);
        
        this.setLocation(x, y);
        
        int width = Properties.getInt(Properties.WINDOW_WIDTH);
        int height = Properties.getInt(Properties.WINDOW_HEIGHT);
        
        this.setSize(width, height);
        
        this.preferredDimension = this.getSize();
        
        this.setSize(this.preferredDimension);
    }
    
    private final void saveProperties0() {
        int extendedState = this.getExtendedState();
        
        Properties.setProperty(Properties.WINDOW_EXTENDED_STATE, extendedState);
        
        if (extendedState == JFrame.MAXIMIZED_BOTH) {
            Dimension preferredDimension = this.getPreferredDimension();
            
            Properties.setProperty(Properties.WINDOW_X, nonMaximizedX);
            Properties.setProperty(Properties.WINDOW_Y, nonMaximizedY);
            
            Properties.setProperty(Properties.WINDOW_WIDTH,
                preferredDimension.width);
            
            Properties.setProperty(Properties.WINDOW_HEIGHT,
                preferredDimension.height);
        }
        else {
            Properties.setProperty(Properties.WINDOW_X, this.getX());
            Properties.setProperty(Properties.WINDOW_Y, this.getY());
            Properties.setProperty(Properties.WINDOW_WIDTH, this.getWidth());
            Properties.setProperty(Properties.WINDOW_HEIGHT, this.getHeight());
        }
        
        Properties.setProperty(Properties.TOOL_BAR_VISIBILITY,
            this.getToolBar().isVisible());
        Properties.setProperty(Properties.STATUS_BAR_VISIBILITY,
            this.getStatusBar().isVisible());
        
        this.saveProperties();
    }
    
    public void saveProperties() {
        // stub
    }

    public void showAboutDialog() {
        String message = Properties.getProperty(Properties.ABOUT_MESSAGE);
        
        JOptionPane.showMessageDialog(this, message, "About " +
            this.getApplicationTitleString(), JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Invoked the first time a window is made visible.
     */
    public void windowOpened(WindowEvent e) {}

    /**
     * Invoked when the user attempts to close the window
     * from the window's system menu.
     */
    public void windowClosing(WindowEvent e) {
        saveProperties0();
        Properties.exportProperties();
        
        controller.exit();
    }

    /**
     * Invoked when a window has been closed as the result
     * of calling dispose on the window.
     */
    public void windowClosed(WindowEvent e) {
        System.exit(0);
    }

    /**
     * Invoked when a window is changed from a normal to a
     * minimized state. For many platforms, a minimized window 
     * is displayed as the icon specified in the window's 
     * iconImage property.
     * @see java.awt.Frame#setIconImage
     */
    public void windowIconified(WindowEvent e) {}

    /**
     * Invoked when a window is changed from a minimized
     * to a normal state.
     */
    public void windowDeiconified(WindowEvent e) {}

    /**
     * Invoked when the Window is set to be the active Window. Only a Frame or
     * a Dialog can be the active Window. The native windowing system may
     * denote the active Window or its children with special decorations, such
     * as a highlighted title bar. The active Window is always either the
     * focused Window, or the first Frame or Dialog that is an owner of the
     * focused Window.
     */
    public void windowActivated(WindowEvent e) {}

    /**
     * Invoked when a Window is no longer the active Window. Only a Frame or a
     * Dialog can be the active Window. The native windowing system may denote
     * the active Window or its children with special decorations, such as a
     * highlighted title bar. The active Window is always either the focused
     * Window, or the first Frame or Dialog that is an owner of the focused
     * Window.
     */
    public void windowDeactivated(WindowEvent e) {}
    
    private int nonMaximizedX = 0;
    private int nonMaximizedY = 0;
    
    public void componentResized(ComponentEvent e) {
        if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
            return;
        }
        else {
            nonMaximizedX = getX();
            nonMaximizedY = getY();
        }
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }
}
