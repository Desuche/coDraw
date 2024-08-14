package org.gui.studioselection;

import org.discovery.DiscoveryService;
import org.gui.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudioSelectionUI extends JDialog {
    private static StudioSelectionUI instance = null;
    protected final JFrame parentElement;
    protected final JPanel serverListPanel;

    protected boolean isActive = true;


    public static StudioSelectionUI getInstance() {
        if (instance == null)
            instance = new StudioSelectionUI(UI.getInstance());
        return instance;
    }

    protected static void kill(){
        instance = null;
    }


    private StudioSelectionUI(JFrame parentElement) {
        super(parentElement, true);
        this.parentElement = parentElement;

        DiscoveryService.sendBroadcastToServers();

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        serverListPanel = new JPanel();
        serverListPanel.setLayout(new BoxLayout(serverListPanel, BoxLayout.Y_AXIS));
        serverListPanel.setBorder(new EmptyBorder(15, 15, 5, 15));

        System.out.println("getting servers");
        for (String[] server : DiscoveryService.getServers()) {
            String studioName = server[0]+ "'s studio";
            JButton button = new JButton(studioName);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    isActive = false;
                    dispose();
                    StudioSelectionService.joinServer(server);
                }
            });
            serverListPanel.add(button);
        }

        JScrollPane scrollPane = new JScrollPane(serverListPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel buttomBar = new JPanel(new BorderLayout(0, 3));

        JButton inviteCodeButton = new JButton("Use Invite Code");
        inviteCodeButton.setPreferredSize(new Dimension(130, 20));
        inviteCodeButton.addActionListener((ActionEvent e) -> {
            StudioSelectionService.processInviteCode(this);
        });

        JPanel inviteCodeButtonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        inviteCodeButtonContainer.add(inviteCodeButton);

        JButton startServerButton = new JButton("Start your own studio");
        startServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isActive = false;
                dispose();
                StudioSelectionService.startServer();
            }
        });
        startServerButton.setBorder(new EmptyBorder(20, 5, 20, 5));

        buttomBar.add(inviteCodeButtonContainer, BorderLayout.NORTH);

        buttomBar.add(startServerButton, BorderLayout.CENTER);


        JPanel container = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Choose a studio");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 15.0F));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(buttomBar, BorderLayout.SOUTH);
        container.setPreferredSize(new Dimension(300, 300));
        getContentPane().add(container);
        pack();
        setLocationRelativeTo(parentElement);

    }


}