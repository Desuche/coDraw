package org.discovery;

import org.utils.NetworkUtils;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class DiscoveryRequestBroadcaster {
    private static DiscoveryRequestBroadcaster instance = null;
    DatagramSocket socket;
    int port = NetworkUtils.getFreeUDPPort();
    ArrayList<String[]> servers;
    DatagramSocket responderSocket;


    protected static DiscoveryRequestBroadcaster getInstance() {
        if (instance == null) {
            instance = new DiscoveryRequestBroadcaster();
        }
        return instance;
    }


    private DiscoveryRequestBroadcaster() {
        servers = new ArrayList<>();
        try {
            socket = new DatagramSocket(port);

        } catch (SocketException e) {
            System.out.println(port);
            throw new RuntimeException(e);
        }

        sendServerFindRequest();
    }



    private void sendServerFindRequest() {
        try {
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
            String str = "Servers please respond!";
            for (int port : DiscoveryService.discoveryServicePortList) {
                DatagramPacket packet = new DatagramPacket(str.getBytes(), str.length(), broadcastAddress, port);
                socket.send(packet);
            }
        } catch (IOException e) {
            System.out.println("Server finding failed");
            e.printStackTrace();
        }

        //wait for response from servers
        Thread t = new Thread(() -> {
            while (true) {
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                try {
                    socket.receive(packet);
                } catch (SocketException e) {
                    System.out.println(e);
                    return;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                byte[] data = packet.getData();
                String str = new String(data, 0, packet.getLength());
                if (str.startsWith("Server:")) {
                    //Server:~<username>~<server port>
                    System.out.println(str);
                    String[] serverData = str.split("~");
                    String username = serverData[1];
                    String[] server= new String[3];
                    server[0]=username;
                    server[1]= packet.getAddress().getHostName();
                    server[2] = serverData[2];
                    servers.add(server);
                }
            }
        });
        t.start();

    }


}
