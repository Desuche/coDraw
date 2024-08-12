package org.gui.chat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChatMessage extends ChatItem {
    private final String content;


    /**
     * Message constructor.
     *
     * @param content -- message content
     * @param sender  -- username of the message sender.
     */
    public ChatMessage(String content, String sender) {
        super(sender);
        this.content = content;


        JTextArea inner = new JTextArea(this.content, 0, 10);
        inner.setSize(new Dimension(200, inner.getPreferredSize().height));
        inner.setEditable(false);
        inner.setLineWrap(true);
        inner.setWrapStyleWord(true);
        inner.setBackground(super.messageColor);
        inner.setForeground(super.textColor);
        inner.setFont(inner.getFont().deriveFont(Font.PLAIN, 12.0F));
        inner.setBorder(new EmptyBorder(0, 0, 8, 0));


        super.panel.add(inner, BorderLayout.CENTER);

        setSize(new Dimension(200, super.panel.getPreferredSize().height));
    }


}
