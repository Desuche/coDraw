package org.gui.chat;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ChatArea extends JPanel {
    private static ChatArea instance;
    private int lastMessagePosition = 0;

    private JScrollBar scrollBar = null;

    public static ChatArea getInstance() {
        if (instance == null) {
            instance = new ChatArea();
        }
        return instance;
    }

    public static void clean(){
        ChatArea.getInstance().removeAll();
        ChatArea.getInstance().lastMessagePosition = 0;
    }

    public void setScrollBar(JScrollBar sb){
        scrollBar = sb;
    }

    private void scrollToButton(){
        if (scrollBar == null) return;
        scrollBar.setValue(scrollBar.getMaximum());
    }

    private ChatArea() {
        setLayout(null);
    }

    public void addMessage(String content, String sender) {

        ChatMessage message = new ChatMessage(content, sender);
        message.setBounds(message.getChatXPosition(), lastMessagePosition + 20, message.getChatWidth(), message.getHeight());
        lastMessagePosition = lastMessagePosition + 20 + message.getHeight();
        setPreferredSize(new Dimension(0, lastMessagePosition));
        SwingUtilities.invokeLater(() -> {
            super.add(message);
            SwingUtilities.invokeLater(this::scrollToButton);
            revalidate();
            repaint();
        });


    }

    public void addFileMessage(File outputFile, String sender) {
        ChatFile message = new ChatFile(outputFile, sender);
        message.setBounds(message.getChatXPosition(), lastMessagePosition + 20, message.getChatWidth(), message.getHeight());
        lastMessagePosition = lastMessagePosition + 20 + message.getHeight();
        setPreferredSize(new Dimension(0, lastMessagePosition));
        SwingUtilities.invokeLater(() -> {
            super.add(message);
            SwingUtilities.invokeLater(this::scrollToButton);
            revalidate();
            repaint();
        });
    }

    public void addImageMessage(File outputFile, String sender) {

        ChatImage message = new ChatImage(outputFile, sender);
        message.setBounds(message.getChatXPosition(), lastMessagePosition + 20, message.getChatWidth(), message.getHeight());
        lastMessagePosition = lastMessagePosition + 20 + message.getHeight();
        setPreferredSize(new Dimension(0, lastMessagePosition));
        SwingUtilities.invokeLater(() -> {
            super.add(message);
            SwingUtilities.invokeLater(this::scrollToButton);
            revalidate();
            repaint();
        });

    }

    public void addBannerAlert(String content){
        BannerAlert alert = new BannerAlert(content);
        alert.setBounds(0, lastMessagePosition + 20, 300, alert.getHeight());
        lastMessagePosition = lastMessagePosition + 20 + alert.getHeight();
        SwingUtilities.invokeLater(() -> {
            super.add(alert);
            SwingUtilities.invokeLater(this::scrollToButton);
            revalidate();
            repaint();
        });

    }


}



