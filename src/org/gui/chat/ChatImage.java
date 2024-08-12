package org.gui.chat;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChatImage extends ChatItem {
    private final File file;


    public ChatImage(File file, String sender) {
        super(sender);
        this.file = file;


        JLabel inner = fetchImageFromFile(file);

        JPanel footer = new JPanel(new GridLayout(0, 1));

        if (!super.isMyMessage) {
            JPanel saveButtonHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
            saveButtonHolder.setBackground(super.messageColor);
            JButton saveButton = new JButton("Save");
            saveButtonHolder.add(saveButton);
            saveButton.setPreferredSize(new Dimension(80, 30));
            saveButton.setHorizontalAlignment(SwingConstants.CENTER);
            saveButton.setFont(saveButton.getFont().deriveFont(Font.BOLD, 10.0F));
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    downloadImage();
                }
            });
            footer.add(saveButtonHolder);
        }


        JTextField timestamp = new JTextField(super.timestamp.toString());
        timestamp.setBackground(super.messageColor);
        timestamp.setForeground(super.textColor);
        timestamp.setHorizontalAlignment(SwingConstants.RIGHT);
        timestamp.setFont(footer.getFont().deriveFont(Font.ITALIC, 9.0F));
        timestamp.setEditable(false);
        footer.add(timestamp);

        footer.setBackground(super.messageColor);
        footer.setForeground(super.textColor);
        footer.setBorder(new EmptyBorder(10, 0, 0, 0));

        super.panel.add(inner, BorderLayout.CENTER);
        super.panel.add(footer, BorderLayout.SOUTH);

        setSize(new Dimension(250, super.panel.getPreferredSize().height));
    }


    private JLabel fetchImageFromFile(File imageFile) {
        try {
            // Read the image file into a BufferedImage
            BufferedImage originalImage = ImageIO.read(imageFile);

            // Calculate the desired width and heisght based on the available space
            int maxWidth = 230;
            int maxHeight = 800;
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            double widthRatio = (double) maxWidth / originalWidth;
            double heightRatio = (double) maxHeight / originalHeight;
            double scaleFactor = Math.min(widthRatio, heightRatio);
            int newWidth = (int) (originalWidth * scaleFactor);
            int newHeight = (int) (originalHeight * scaleFactor);

            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            ImageIcon imageIcon = new ImageIcon(scaledImage);

            JLabel imageLabel = new JLabel(imageIcon);
            return imageLabel;


        } catch (IOException e) {
            e.printStackTrace();
            JLabel errorMsg = new JLabel("**Image failed to load**");
            errorMsg.setFont(errorMsg.getFont().deriveFont(Font.ITALIC, 12.0F));
            return errorMsg;
        }
    }

    private void downloadImage() {
        ChatUtils.downloadChatImage(this.file);
    }


}