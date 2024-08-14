package org.utils;

import org.servers.internalserver.InternalServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class NetworkUtils {
    private static Logger logger = Logger.getLogger("global");
    static String[] publicIpEchoEndpoints = new String[]{
            "https://checkip.amazonaws.com/",
            "https://ipv4.icanhazip.com/",
            "http://myexternalip.com/raw",
            "http://ipecho.net/plain"
    };

    public static int getFreeUDPPort(){
        logger.info("getting free UDP ports");
        int min = 15000;
        int max = 16000;

        for (int port = min; port <= max; port++){
            logger.info("Checking UDP port " + port);
            try (DatagramSocket testSocket = new DatagramSocket(port)){
                logger.info("Found free UDP port " + port);
                return port;
            } catch (ConnectException e){
            } catch (IOException e){
            }
        }
        logger.info("No free port found");
        System.exit(1);
        return 0;
    }

    public static int getFreeTCPPort(){
        logger.info("getting free TCP ports");
        int min = 11000;
        int max = 52000;

        for (int i = 0; i < 5000; i++){
            int port = ThreadLocalRandom.current().nextInt(min, max);
            logger.info("Checking TCP port " + port);
            try (ServerSocket testSocket = new ServerSocket(port)){
                logger.info("Found free TCP port " + port);
                return port;
            } catch (ConnectException e){
            } catch (IOException e){
            }
        }
        logger.severe("No free port found");
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
                    logger.info(myPublicIp);
                    break;
                } catch (IOException e) {
                    logger.info("Failed to read my public ip from " + endpoint);
                }
            }

            if (myPublicIp.isEmpty()) {
                logger.info("Generating Invitation Code failed");
            }

            return myPublicIp;

        } catch (MalformedURLException e) {
            logger.severe("MalformedURL:" + e);
            return "";
        }
    }
}
