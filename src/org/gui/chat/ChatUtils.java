package org.gui.chat;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ChatUtils {

    protected static void downloadChatFile(File chatFile) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setName("Save File");
            int option = fileChooser.showSaveDialog(ChatArea.getInstance());
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getAbsolutePath().contains(".")){
                    selectedFile = new File(selectedFile.getAbsolutePath() + "." + chatFile.getName().split("\\.")[1]);
                }

                Files.copy(chatFile.toPath(), selectedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                JOptionPane.showMessageDialog(ChatArea.getInstance(),
                        "File downloaded to " + selectedFile.getCanonicalPath());
            } else {
                return;
            }
        } catch (IOException e) {
            System.out.println("Download file failed");
        }
    }

    protected static void downloadChatImage(File chatImage) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setName("Save Image");
            FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
                    "Image Files", "jpg", "jpeg", "png", "gif", "bmp", "webp");
            fileChooser.setFileFilter(imageFilter);
            int option = fileChooser.showSaveDialog(ChatArea.getInstance());
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getAbsolutePath().contains(".")){
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".jpeg");
                }

                Files.copy(chatImage.toPath(), selectedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                JOptionPane.showMessageDialog(ChatArea.getInstance(),
                        "Image downloaded to " + selectedFile.getCanonicalPath());
            } else {
                return;
            }
        } catch (IOException e) {
            System.out.println("Download image failed");
        }
    }
}
