package org.gui.studioselection;

import org.KidPaint;
import org.gui.UI;
import org.gui.chat.ChatArea;
import org.server.ExternalConnectedServer;
import org.server.InternalServer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudioSelectionServices {
    protected static void joinServer(String[] server) {
        //create new external server with correct credentials
        ExternalConnectedServer.getInstance().initialiseServer(server);
    }

    protected static void startServer() {
        ChatArea.getInstance().addBannerAlert("You've started your own studio!");
        InternalServer.getInstance();
        UI.getInstance().internalServerSetup();
    }

    protected static void processInviteCode(StudioSelectionUI studioSelectionUI) {

        JDialog dialog = new JDialog(studioSelectionUI, true);

        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(studioSelectionUI.parent);
        dialog.setLayout(new BorderLayout());

        JLabel label = new JLabel("Enter a valid invitation code");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 15.0F));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        dialog.add(label, BorderLayout.NORTH);

        JTextField inputField = new JTextField();
        inputField.setBorder(new EmptyBorder(0, 10, 0, 10));
        inputField.setHorizontalAlignment(SwingConstants.CENTER);
        dialog.add(inputField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JLabel errorMsg = new JLabel("");
        errorMsg.setForeground(Color.RED);
        errorMsg.setHorizontalAlignment(SwingConstants.CENTER);

        JButton okButton = new JButton("Use Code");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] decodedCode = KidPaint.decode(inputField.getText());
                studioSelectionUI.isActive = false;
                dialog.dispose();
                studioSelectionUI.dispose();
                joinServer(new String[]{"Remote User", decodedCode[0], decodedCode[1]});
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });



        buttonPanel.add(errorMsg, BorderLayout.NORTH);

        buttonPanel.add(okButton, BorderLayout.WEST);
        buttonPanel.add(cancelButton, BorderLayout.EAST);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.show();
    }

}
