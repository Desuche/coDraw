package org.servers.proxyserver;

import org.gui.chat.ChatArea;
import org.CoDraw;
import org.gui.peripherials.UserNameInput;
import org.servers.ServerMessage;

import java.io.*;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.Socket;

/**
 * This object represents a proxy for an external server which this coDraw instance is connected to
 * */
public class ExternalConnectedServer {
    private static ExternalConnectedServer instance = null;
    private boolean isIntialised = false;
    private Socket externalSocket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;


    public static ExternalConnectedServer getInstance() {
        if (instance == null) {
            instance = new ExternalConnectedServer();
        }
        return instance;
    }

    private ExternalConnectedServer() {

    }

    public static void kill(){
        try {
            System.out.println("killing socket now");
            if (getInstance().externalSocket != null) getInstance().externalSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        instance = null;
    }

    public void initialiseServer(String[] serverDetails) {
        String username = serverDetails[0];
        String ip = serverDetails[1];
        int port = Integer.parseInt(serverDetails[2]);
        System.out.println("Connected server's port is " + port);
        InetAddress server;
        try {
            server = InetAddress.getByName(ip);
            externalSocket = new Socket(server.getHostAddress(), port);
            in = new DataInputStream(externalSocket.getInputStream());
            out = new DataOutputStream(externalSocket.getOutputStream());
        } catch (Exception e) {
            System.out.println("This is an immediate exception " + e.getMessage());
            CoDraw.core.serverNotFound();
            return;
        }

        listenToExternalServer();
        this.isIntialised = true;

        ChatArea.getInstance().addBannerAlert("Joining " + username + "'s studio ");

    }

    public void sendChatMessage(String message) {
        String username = UserNameInput.getUserName();
        try {

            out.writeInt(ServerMessage.ChatMessage.value);//type
            out.writeInt(username.length());
            out.write(username.getBytes(), 0, username.length());
            out.writeInt(message.length());
            out.write(message.getBytes(), 0, message.length());
            out.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendPixelMessage(int color, int x, int y,  int penType,int penSize) {

        try {

            out.writeInt(ServerMessage.PixelMessage.value); // type
            out.writeInt(color);
            out.writeInt(x);
            out.writeInt(y);
            out.writeInt(penType);
            out.writeInt(penSize);
            out.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendBucketMessage(int x, int y, int color) {

        try {

            out.writeInt(ServerMessage.BucketMessage.value); // type
            out.writeInt(color);
            out.writeInt(x);
            out.writeInt(y);
            out.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendDataLoadMessage(File loadFile){
        File file=loadFile;
        String username = UserNameInput.getUserName();
        try {
            FileInputStream in = new FileInputStream(file);
            byte[] buffer = new byte[1024];

            out.writeInt(ServerMessage.DataLoadMessage.value); // type

            out.writeInt(username.length());
            out.write(username.getBytes(), 0, username.length());

            out.writeInt(file.getName().length());
            out.write(file.getName().getBytes());

            long size = file.length();
            out.writeLong(size);

            System.out.printf("Uploading file %s (%d)", file.getName(), size);
            while(size > 0) {
                int len = in.read(buffer, 0, buffer.length);
                out.write(buffer, 0, len);
                size -= len;
                System.out.print(".");
            }

        } catch (Exception e) {
            System.err.println("Unable upload file " + file);
            e.printStackTrace();
        }
        System.out.println("\nUpload completed.");


    }

    public void sendFileMessage(File file) {
        String username = UserNameInput.getUserName();
        try {
            FileInputStream in = new FileInputStream(file);
            byte[] buffer = new byte[1024];

            out.writeInt(ServerMessage.FileMessage.value); // type

            out.writeInt(username.length());
            out.write(username.getBytes(), 0, username.length());

            out.writeInt(file.getName().length());
            out.write(file.getName().getBytes());

            long size = file.length();
            out.writeLong(size);

            System.out.printf("Uploading file %s (%d)", file.getName(), size);
            while(size > 0) {
                int len = in.read(buffer, 0, buffer.length);
                out.write(buffer, 0, len);
                size -= len;
                System.out.print(".");
            }

        } catch (Exception e) {
            System.err.println("Unable upload file " + file);
            e.printStackTrace();
        }
        System.out.println("\nUpload completed.");
    }

    public void sendImageMessage(File file) {
        String username = UserNameInput.getUserName();
        try {
            FileInputStream in = new FileInputStream(file);
            byte[] buffer = new byte[1024];

            out.writeInt(ServerMessage.ImageMessage.value); // type

            out.writeInt(username.length());
            out.write(username.getBytes(), 0, username.length());

            out.writeInt(file.getName().length());
            out.write(file.getName().getBytes());

            long size = file.length();
            out.writeLong(size);

            System.out.printf("Uploading image %s (%d)", file.getName(), size);
            while(size > 0) {
                int len = in.read(buffer, 0, buffer.length);
                out.write(buffer, 0, len);
                size -= len;
                System.out.print(".");
            }
            System.out.println("\nUpload completed.");
        } catch (Exception e) {
            System.err.println("Unable upload file " + file);
            e.printStackTrace();
        }

    }


    private void listenToExternalServer() {
        System.out.println("Listening to external server:");
        Thread t = new Thread(() -> {
            System.out.println(externalSocket.getInetAddress().getHostName());
            while (true) {
                try {
                    ServerMessage message = ServerMessage.getServerMessage(in.readInt());

                    switch (message) {
                        case ChatMessage:
                            MessageHandler.handleChatMessage(in);
                            break;
                        case PixelMessage:
                            MessageHandler.handlePixelMessage(in);
                            break;
                        case BucketMessage:
                            MessageHandler.handleBucketMessage(in);
                            break;
                        case DataLoadMessage:
                            MessageHandler.handleDataLoadMessage(in);
                            break;
                        case FileMessage:
                            MessageHandler.handleFileMessage(in);
                            break;
                        case ImageMessage:
                            MessageHandler.handleImageMessage(in);
                            break;
                        default:
                            System.out.println("Unknown message type received");
                    }

                } catch (SocketException e) {
                    System.out.println("External server is down");
                    CoDraw.core.restart();
                    return;
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

        });
        t.start();
    }


}


