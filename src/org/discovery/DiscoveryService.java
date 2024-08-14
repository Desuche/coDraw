package org.discovery;

import java.util.ArrayList;
import java.util.List;

public class DiscoveryService {
    private DiscoveryService(){}
    protected static int[] discoveryServicePortList = new int[]{22222,33333,44444};

    public static void kill(){
        DiscoveryRequestBroadcaster sf = DiscoveryRequestBroadcaster.getInstance();
        sf.socket.close();
        if (sf.responderSocket != null) sf.responderSocket.close();

    }

    public static void launchDiscoveryListener(int serverPort){
         if (!(new DiscoveryBroadcastListener(serverPort)).start()){
            //Replace graceful teardown
            System.exit(1);
        }
    }

    public static void sendBroadcastToServers(){
        DiscoveryRequestBroadcaster.getInstance();
    }

    public static List<String[]> getServers(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e){
        }
        return DiscoveryRequestBroadcaster.getInstance().servers;
    }
}
