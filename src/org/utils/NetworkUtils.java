package org.utils;

import org.servers.internalserver.InternalServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.concurrent.ThreadLocalRandom;

public class NetworkUtils {
    static String[] publicIpEchoEndpoints = new String[]{
            "https://checkip.amazonaws.com/",
            "https://ipv4.icanhazip.com/",
            "http://myexternalip.com/raw",
            "http://ipecho.net/plain"
    };

    public static int getFreeUDPPort(){
        System.out.println("getting free UDP ports");
        int min = 15000;
        int max = 16000;

        for (int port = min; port <= max; port++){
            System.out.println("Checking UDP port " + port);
            try (DatagramSocket testSocket = new DatagramSocket(port)){
                System.out.println("Found free UDP port " + port);
                return port;
            } catch (ConnectException e){
            } catch (IOException e){
            }
        }
        System.out.println("No free port found");
        System.exit(1);
        return 0;
    }

    public static int getFreeTCPPort(){
        System.out.println("getting free TCP ports");
        int min = 11000;
        int max = 52000;

        for (int i = 0; i < 5000; i++){
            int port = ThreadLocalRandom.current().nextInt(min, max);
            System.out.println("Checking TCP port " + port);
            try (ServerSocket testSocket = new ServerSocket(port)){
                System.out.println("Found free TCP port " + port);
                return port;
            } catch (ConnectException e){
            } catch (IOException e){
            }
        }
        System.out.println("No free port found");
        System.exit(1);
        return 0;
    }

    public static String getPublicIpAddress() {
        try {

            String myPublicIp = "";

            for (String endpoint : publicIpEchoEndpoints) {
                URL url = new URL(endpoint);
                try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    myPublicIp = (br.readLine());
                    System.out.println(myPublicIp);
                    break;
                } catch (IOException e) {
                    System.out.println("Failed to read my public ip from " + endpoint);
                }
            }

            if (myPublicIp.isEmpty()) {
                System.out.println("Generating Invitation Code failed");
            }

            return myPublicIp;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
    }
}
