/*
 * JDirectoryChooser.java
 *
 * Created on September 21, 2006, 1:26 PM
 */

package org.rummage.slide;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author nnelson
 */
public class JDirectoryChooser
    extends JDialog
{
    private int selectedOption = 0;
    private File selectedDirectory = null;
    
    public JDirectoryChooser() {
        this(null);
    }
    
    public JDirectoryChooser(Component owner) {
        super((JFrame)null, true);
        final JFileSystemTree tree = new JFileSystemTree();
        final JButton approveButton = new JButton("Choose");
        final JButton cancelButton = new JButton("Cancel");
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(approveButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        getContentPane().add(panel);
        
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object c = e.getSource();
                if (c == approveButton || c == tree) {
                    selectedOption = JFileChooser.APPROVE_OPTION;
                    selectedDirectory = tree.getSelectedDirectory();
                }
                setVisible(false);
            }
        };
        
        tree.addActionListener(actionListener);
        approveButton.addActionListener(actionListener);
        cancelButton.addActionListener(actionListener);
        
        tree.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
                if (ev.getPropertyName().equals("selectedDirectory")) {
                    approveButton.setEnabled(tree.
                        getSelectedDirectory() != null);
                }
            }
        });
        
        setSize(new Dimension(300, 350));
        setPreferredSize(new Dimension(300, 350));
        setLocationRelativeTo(owner);
        tree.scrollRowToVisible(Math.max(0, tree.getMinSelectionRow()-4));
        setVisible(true);
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public File getSelectedDirectory() {
        return selectedDirectory;
    }
    
    public File getCurrentDirectory() {
        return getSelectedDirectory();
    }
}
