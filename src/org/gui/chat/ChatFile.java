package org.gui.chat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ChatFile extends ChatItem {
    private final File file;


    public ChatFile(File file, String sender) {
        super(sender);
        this.file = file;

        JPanel inner = new JPanel(new FlowLayout());
        JLabel label = new JLabel(file.getName());
        label.setPreferredSize(new Dimension(150, 30));
        inner.add(label);
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downloadFile();
            }
        });
        inner.add(saveButton);


        super.header.setBorder(new EmptyBorder(0, 0, 10, 0));
        super.footer.setBorder(new EmptyBorder(10, 0, 0, 0));

        super.panel.add(inner, BorderLayout.CENTER);

        setSize(new Dimension(250, super.panel.getPreferredSize().height));


    }

    private void downloadFile() {
        ChatUtils.downloadChatFile(this.file);
    }
}
