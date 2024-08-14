package org.discovery;

import org.gui.peripherials.UserNameInput;
import org.servers.internalserver.InternalServer;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class DiscoveryBroadcastListener {
    protected DatagramSocket responderSocket;
    private int internalServerPort;
    protected DiscoveryBroadcastListener(int internalServerPort){
        this.internalServerPort = internalServerPort;
    }

    protected boolean start(){
        DatagramSocket socket = null;

        for (int i = 0; i < DiscoveryService.discoveryServicePortList.length; i++){
            int responderPort = DiscoveryService.discoveryServicePortList[i];
            try {
                socket = new DatagramSocket(responderPort);
                System.out.println("Setting up server responder at port" + responderPort);
                break;
            } catch (SocketException e){
            }
            if (i == DiscoveryService.discoveryServicePortList.length - 1){
                System.out.println("Maximum Kidpaint servers active");
                System.out.println(" ( could be that kidpaint ports are in use by other system processes )");
                return false;
            }
        }

        this.responderSocket = socket;

        launchBroadcastResponder();
        return true;
    }

    private void launchBroadcastResponder(){

        Thread t = new Thread(() -> {
            while (true) {
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                try {
                    responderSocket.receive(packet);
                } catch (SocketException e) {
                    System.out.println(e);
                    return;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                byte[] data = packet.getData();
                String str = new String(data, 0, packet.getLength());
                InetAddress srcAddr = packet.getAddress();
                int srcPort = packet.getPort();
                if (str.equals("Servers please respond!") && InternalServer.isRunning()){
                    String myUsername = UserNameInput.getUserName();
                    String strr = "Server:~"+myUsername + "~" + internalServerPort;
                    packet = new DatagramPacket(strr.getBytes(), strr.length(), srcAddr, srcPort);
                    try {
                        responderSocket.send(packet);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        });
        t.start();
    }


}
