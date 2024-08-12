package org.gui.chat;

import org.gui.UserNameInput;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Date;

abstract class ChatItem extends JPanel {
    protected final String sender;
    protected final boolean isMyMessage;
    protected final Color messageColor;
    protected final Color textColor;

    protected final Date timestamp;

    protected final JTextField header;

    protected final JTextField footer;

    protected final JPanel panel;


    protected final int xPosition;
    protected final int width = 250;

    protected int getChatXPosition(){
        return this.xPosition;
    }

    protected int getChatWidth(){
        return this.width;
    }


    protected ChatItem(String sender){
        this.sender = sender;
        this.isMyMessage = UserNameInput.getUserName().equals(sender);
        this.messageColor = this.isMyMessage ? Color.blue : Color.LIGHT_GRAY;
        this.textColor = this.isMyMessage ? Color.white : Color.black;
        this.xPosition = isMyMessage ? 30 : 5;
        this.timestamp = new Date();


        this.setLayout(new BorderLayout());

        panel = new JPanel(new BorderLayout());

        header = new JTextField(this.sender);
        header.setBackground(messageColor);
        header.setForeground(textColor);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 15.0F));
        header.setEditable(false);
        header.setBorder(new EmptyBorder(0, 0, 0, 0));

        footer = new JTextField(this.timestamp.toString());
        footer.setBackground(messageColor);
        footer.setForeground(textColor);
        footer.setHorizontalAlignment(SwingConstants.RIGHT);
        footer.setFont(footer.getFont().deriveFont(Font.ITALIC, 9.0F));
        footer.setEditable(false);
        footer.setBorder(new EmptyBorder(0, 0, 0, 0));


        panel.add(header, BorderLayout.NORTH);
        panel.add(footer, BorderLayout.SOUTH);
        panel.setBorder(new EmptyBorder(10, 5, 2, 5));
        panel.setBackground(messageColor);



        if (isMyMessage)
            add(panel, BorderLayout.EAST);
        else
            add(panel, BorderLayout.WEST);
        add(new JPanel(), BorderLayout.CENTER);

    }

}

