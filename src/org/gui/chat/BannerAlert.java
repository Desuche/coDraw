package org.gui.chat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BannerAlert extends JPanel {


    public BannerAlert(String content) {
        JLabel label = new JLabel(content);
        add(label);
        setBackground(Color.yellow);
        setBorder(new EmptyBorder(10, 0,10,0));
        setSize(new Dimension(300, getPreferredSize().height));
    }
}