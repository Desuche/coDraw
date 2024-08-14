package org.core;

import org.gui.UI;
import org.gui.chat.ChatArea;
import org.gui.peripherials.UserNameInput;
import org.gui.studioselection.StudioSelectionService;
import org.gui.studioselection.StudioSelectionUI;
import org.servers.internalserver.InternalServer;
import org.servers.proxyserver.ExternalConnectedServer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class Core {

    private int rows;
    private int cols;
    private int block_size;
    private int width;
    private int height ;

    public Core(int rows, int cols, int block_size, int width, int height){
        this.rows = rows;
        this.cols = cols;
        this.block_size = block_size;
        this.width = width;
        this.height = height;
    }

    public void start(){
        UI ui = UI.getInstance();            // get the instance of UI

        ui.setData(new int[rows][cols], block_size);    // set the data array and block size. comment this statement to use the default data array and block size.
        ui.setSize(new Dimension(width, height));
        ui.setVisible(true);                // set the ui

        UserNameInput userNameInput = UserNameInput.getInstance();
        StudioSelectionUI studioSelectionUI = StudioSelectionUI.getInstance();

        userNameInput.setVisibleAfterClose(studioSelectionUI);
        userNameInput.setVisible(true);  // launch the username dialog box
    }

    public void restart() {
        saveCurrentDrawing();
        restartComponents();

    }

    public void serverNotFound() {
        JOptionPane.showMessageDialog(
                null,
                "Studio is no longer online!",
                "Error",
                JOptionPane.WARNING_MESSAGE
        );
        restartComponents();
    }

    private void restartComponents(){
        StudioSelectionService.kill();

        if (InternalServer.isRunning()){
            InternalServer.getInstance().kill();
        }
        if (ExternalConnectedServer.getInstance() != null){
            ExternalConnectedServer.getInstance().kill();
        }


        rebuildUI();

        StudioSelectionUI studioSelectionUI = StudioSelectionUI.getInstance();
        studioSelectionUI.setVisible(true);
    }

    private void rebuildUI(){
        UI ui = UI.getInstance();
        ui.setData(new int[rows][cols], block_size);
        ui.setSize(new Dimension(width, height));
        ChatArea.clean();
        ui.revalidate();
        ui.repaint();
    }

    private void saveCurrentDrawing(){
        int result = JOptionPane.showConfirmDialog(UI.getInstance(), "Do you want to save drawing?", "Studio Offline",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);


        if (result == JOptionPane.YES_OPTION) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter fileFiler = new FileNameExtensionFilter(
                    "CoDraw Files", "kpt");
            fileChooser.setFileFilter(fileFiler);
            int option = fileChooser.showSaveDialog(UI.getInstance());
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getAbsolutePath().contains(".")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".kpt");
                }
                UI.getInstance().saveDrawingToFile(selectedFile);
            }
        }
    }

}
