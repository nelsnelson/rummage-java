/*
 * FindDialog.java
 *
 * Created on June 1, 2006, 2:28 PM
 */

package org.rummage.slide;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.rummage.slide.border.SimpleShadowBorder;

/**
 *
 * @author nnelson
 */
public class FindDialog
    extends JDialog
{
    private Controller controller = createMicroController();
    
    /** Creates a new instance of FindDialog */
    public FindDialog(Dialog owner) {
        super(owner);
        
        initUI();
    }
    
    private void initUI() {
        setTitle("Find");
        SlideUtilities.setDimensions(this, 450, 150, 300, 200);
        
        JPanel panel = new JPanel(new BorderLayout());
        
        panel.add(createPrimaryContent(), BorderLayout.CENTER);
        
        setContentPane(panel);
        setVisible(true);
    }

    private JPanel createPrimaryContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextArea searchTextArea = new JTextArea();
        searchTextArea.setBorder(createFieldBorder("Find what:"));
        searchTextArea.setLineWrap(true);
        SlideUtilities.setPermanentSize(searchTextArea, 250, 26);
        JScrollPane firstScrollPane =
            new JScrollPane(searchTextArea,
            JScrollPane.VERTICAL_SCROLLBAR_NEVER,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, false);
        
        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        
        JButton findButton = new JButton("Find Next");
        findButton.setMnemonic('N');
        findButton.addActionListener(controller);
        
        buttons.add(Box.createHorizontalGlue());
        buttons.add(findButton);
        
        panel.add(searchTextArea, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private Border createFieldBorder(String title) {
        Border b1 = new SimpleShadowBorder();
        Border b2 = BorderFactory.createEmptyBorder();
        Border b3 = BorderFactory.createTitledBorder(b2, title);
        Border b4 = BorderFactory.createCompoundBorder(b3, b1);
        
        return b4;
    }

    private Controller createMicroController() {
        return new Controller() {
            public void doCommand(Command command) {
                if (command.equals("Find Next")) {
                    new org.rummage.slide.JError("Not yet implemented.");
                }
            }
        };
    }
}
