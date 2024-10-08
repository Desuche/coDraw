package org.gui.peripherials;

import org.gui.studioselection.StudioSelectionUI;
import org.gui.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;

public class UserNameInput extends JDialog {
    private static Logger logger = Logger.getLogger("global");
    private static UserNameInput instance = null;
    private final JTextField inputField;
    private String inputText;

    private StudioSelectionUI studioSelectionUI;

    public static UserNameInput getInstance() {
        if (instance == null)
            instance = new UserNameInput(UI.getInstance());
        return instance;
    }

    private UserNameInput(JFrame parent) {
        super(parent, true);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        setSize(300, 150);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Enter your username.");
        label.setFont(label.getFont().deriveFont(Font.BOLD,15.0F));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.NORTH);

        inputField = new JTextField();
        inputField.setBorder(new EmptyBorder(10,10,10,10));
        inputField.setHorizontalAlignment(SwingConstants.CENTER);
        add(inputField, BorderLayout.CENTER);

        inputField.addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) {}
            @Override public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10) {		// if the user press ENTER
                    execute(parent);
                }
            }

        });

        JButton okButton = new JButton("Continue");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                execute(parent);

            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void execute(JFrame parent){
        if (inputField.getText().isEmpty()) return;
        inputText = inputField.getText();
        logger.info("Username is " + inputText);
        ((UI)parent).loadusername(inputText);
        dispose();
        if (studioSelectionUI != null){
            studioSelectionUI.setVisible(true);
        }
    }

    public void setVisibleAfterClose(StudioSelectionUI studioSelectionUI){
        this.studioSelectionUI = studioSelectionUI;
    }


    public static String getUserName(){
        return getInstance().inputText;
    }
}
