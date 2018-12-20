/*
 * JProgressIndicator.java
 *
 * Created on August 15, 2006, 3:14 PM
 */

package org.rummage.slide;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.rummage.info.clearthought.layout.TableLayout;

/**
 *
 * @author nelsnelson
 */
public class JProgressIndicator
    extends JDialog
{
    private static final String title = "Progress";
    
    private int n = 1;
    private int max = 100;
    private Object message = null;
    private JComponent messageHolder = null;
    private JLabel messageLabel = null;
    private JLabel statusLabel = null;
    private javax.swing.JProgressBar progress = null;
    private boolean autoClose = true;
    
    /** Creates a new instance of JProgressIndicator */
    public JProgressIndicator() {
        this(100, null, title);
    }
    
    /** Creates a new instance of JProgressIndicator */
    public JProgressIndicator(int max) {
        this(max, null, title);
    }
    
    /** Creates a new instance of JProgressIndicator */
    public JProgressIndicator(int max, Object message) {
        this(max, message, title);
    }
    
    /** Creates a new instance of JProgressIndicator */
    public JProgressIndicator(int max, Object message, String title) {
        super((JFrame) null, title);
        
        this.max = max;
        this.message = message;
        
        initUI();
    }
    
    private void initUI() {
        double[][] sizes = new double[][] {
            {4.0d, TableLayout.PREFERRED, 4.0d, -1.0d},
            {4.0d, TableLayout.PREFERRED, 4.0d, TableLayout.PREFERRED, 4.0d,
             TableLayout.PREFERRED, 4.0d, TableLayout.FILL}
        };
        
        JPanel panel = new JPanel(new TableLayout(sizes));
        
        messageHolder = new JComponent() {};
        messageHolder.setLayout(new BorderLayout());
        
        messageLabel = new JLabel();
        
        if (message instanceof String) {
            messageLabel = new JLabel((String) message);
            
            messageHolder.add(messageLabel, BorderLayout.CENTER);
        }
        else if (message instanceof JComponent) {
            messageHolder.add((JComponent) message, BorderLayout.CENTER);
        }
        
        panel.add(messageHolder, "1, 1");
        
        statusLabel = new JLabel(" ");
        
        panel.add(statusLabel, "1, 3");
        
        // for now
        progress = new javax.swing.JProgressBar();
        
        progress.setStringPainted(false);
        progress.setValue(0);
        progress.setMaximum(max);
        progress.setIndeterminate(false);
        SlideUtilities.setPermanentSize(progress, 250, 14);
        
        panel.add(progress, "1, 5");
        
        setContentPane(panel);
        setModal(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void setMessage(String message) {
        getMessageLabel().setText(message);
        paint(getGraphics());
    }
    
    public void setStatus(final String status) {
        getStatusLabel().setText(status);
        getProgress().paintImmediately(getBounds());
        paint(getGraphics());
        
        if (getPercentComplete() == 1.0d && getAutoClose()) {
            dispose();
        }
    }
    
    private int getMax() {
        return max;
    }
    
    private boolean getAutoClose() {
        return autoClose;
    }
    
    private JLabel getMessageLabel() {
        return messageLabel;
    }
    
    private JLabel getStatusLabel() {
        return statusLabel;
    }
    
    private javax.swing.JProgressBar getProgress() {
        return progress;
    }
    
    public void update() {
        update(1);
    }
    
    public void update(final int i) {
        n = n + i;
        
        getProgress().setValue(n);
        
        getProgress().paintImmediately(getBounds());
        paint(getGraphics());
        
        if (getPercentComplete() == 1.0d && getAutoClose()) {
            dispose();
        }
    }

    public double getPercentComplete() {
        return progress.getPercentComplete();
    }
}
