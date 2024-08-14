package org.servers.internalserver;

import org.discovery.DiscoveryService;
import org.gui.UI;
import org.gui.peripherials.UserNameInput;
import org.servers.ServerMessage;
import org.utils.EncodingUtils;
import org.utils.NetworkUtils;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class InternalServer {
    private Logger logger = Logger.getLogger("global");
    private static InternalServer instance = null;
    private final ArrayList<Socket> subscribers;
    private final int port;
    ServerSocket internalServerSocket;

    public static InternalServer getInstance() {
        if (instance == null) {
            instance = new InternalServer();
        }

        return instance;
    }
    public static void kill(){
        try {
            if ((instance != null) && (instance.internalServerSocket != null))
                instance.internalServerSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        instance = null;
    }

    public static boolean isRunning() {
        return (instance != null);
    }

    private InternalServer() {
        logger.info("Internal Server starting");
        this.port = NetworkUtils.getFreeTCPPort();
        subscribers = new ArrayList<>();
        DiscoveryService.launchDiscoveryListener(port);


        try {

            internalServerSocket = new ServerSocket(port);

            logger.info("Internal server socket listening at port : " + port);
            Thread runner = new Thread(() -> {
                run();
            });
            runner.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("Internal Server started");
    }

    public String getInvitationCode() {
            String myPublicIp = NetworkUtils.getPublicIpAddress();
            return EncodingUtils.encodeInvitation(myPublicIp, String.valueOf(port));
    }

    private void run() {
        while (true) {

            try {
                Socket clientSocket = internalServerSocket.accept();
                synchronized (subscribers) {
                    subscribers.add(clientSocket);
                }

                Thread t = new Thread(() -> {
                    try {
                        sendStateToNewJoiner(clientSocket);
                        serve(clientSocket);
                    } catch (IOException ex) {

                    }
                    synchronized (subscribers) {
                        subscribers.remove(clientSocket);
                    }

                });
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

        }
    }


    private void serve(Socket clientSocket) throws IOException {
        logger.info("Internal Server is now serving " + clientSocket.getInetAddress());
        DataInputStream in = new DataInputStream(clientSocket.getInputStream());


        while (true) {
            ServerMessage message = ServerMessage.getServerMessage( in.readInt() );
            logger.info("Got message of type " + message + " from" + clientSocket.getInetAddress());

            switch (message) {
                case ChatMessage:
                    MessageHandler.handleChatMessage(in, subscribers);
                    break;
                case PixelMessage:
                    MessageHandler.handlePixelMessage(in, subscribers);
                    break;
                case BucketMessage:
                    MessageHandler.handleBucketMessage(in, subscribers);
                    break;
                case DataLoadMessage:
                    MessageHandler.handleDataLoadMessage(in, subscribers);
                    break;
                case FileMessage:
                    MessageHandler.handleFileMessage(in, subscribers);
                    break;
                case ImageMessage:
                    MessageHandler.handleImageMessage(in, subscribers);
                    break;

                default:

            }

        }
    }

    public void sendMyChatMessage(String chatMessage) {
        try {
            byte[] username = UserNameInput.getUserName().getBytes();
            int userNameLen = username.length;
            byte[] chatBuffer = chatMessage.getBytes();
            int chatLen = chatBuffer.length;

            synchronized (subscribers) {
                for (int i = 0; i < subscribers.size(); i++) {
                    logger.info("Sending data to sub: " + subscribers.get(i));
                    try {
                        Socket s = subscribers.get(i);
                        DataOutputStream out = new DataOutputStream(s.getOutputStream());
                        out.writeInt(0);//type
                        out.writeInt(userNameLen);
                        out.write(username, 0, userNameLen);
                        out.writeInt(chatLen);
                        out.write(chatBuffer, 0, chatLen);
                        out.flush();
                    } catch (IOException ex) {
                        logger.info("Client already disconnected");
                        ex.printStackTrace();
                    }

                }
            }
        } catch (Exception e) {
            logger.info("Error here handle my ownmessage");
            e.printStackTrace();
        }
    }

    public void sendMyPixelMessage(int color, int x, int y, int penType, int penSize) {
        try {
            synchronized (subscribers) {
                for (int i = 0; i < subscribers.size(); i++) {
                    try {
                        Socket s = subscribers.get(i);
                        DataOutputStream out = new DataOutputStream(s.getOutputStream());
                        out.writeInt(1); // type
                        out.writeInt(color);
                        out.writeInt(x);
                        out.writeInt(y);
                        out.writeInt(penType);
                        out.writeInt(penSize);
                        out.flush();
                    } catch (IOException ex) {
                        logger.info("Client already disconnected");
                        ex.printStackTrace();
                    }

                }
            }
        } catch (Exception e) {
            logger.info("Error here handlemyownmessage");
            e.printStackTrace();
        }
    }

    public void sendMyBucketMessage(int color, int x, int y) {
        try {
            synchronized (subscribers) {
                for (int i = 0; i < subscribers.size(); i++) {
                    try {
                        Socket s = subscribers.get(i);
                        DataOutputStream out = new DataOutputStream(s.getOutputStream());
                        out.writeInt(2); // type
                        out.writeInt(color);
                        out.writeInt(x);
                        out.writeInt(y);
                        out.flush();
                    } catch (IOException ex) {
                        logger.info("Client already disconnected");
                        ex.printStackTrace();
                    }

                }
            }
        } catch (Exception e) {
            logger.info("Error here handlemyownmessage");
            e.printStackTrace();
        }
    }

    public void loadMyFile(File file) throws FileNotFoundException {
        //Load file locally

        int[][][] outputDataArray = new int[1][1][1];
        boolean[] success = new boolean[1];
        int[] outputBlockSize = new int[1];
        UI.loadDrawingFromFileIntoDataArray(file, outputDataArray, outputBlockSize, success);
        if (!success[0]) {
            logger.info("Loading failed at server: unable to properly load the file sent by the client");
            return;
        }
        //update the server's own data array
        UI.getInstance().loadDrawingLocally(outputDataArray[0], outputBlockSize[0]);

        //send file to subscribers
        try {
            /**
             *
             * //
             *         //TODO (File is already given as parameter. No need to read anything)
             *         //read file from datainputstream into file object
             * //        in.read(userNameBuffer, 0, userNameLen);
             *
             *
             */

            String Username = UserNameInput.getUserName();
            byte userNameBuffer[] = Username.getBytes();


            synchronized (subscribers) {
                logger.info("" + subscribers.size() + "Subscribers");
                for (int i = 0; i < subscribers.size(); i++) {
                    try {
                        FileInputStream insend = new FileInputStream(file);
                        Socket s = subscribers.get(i);
                        DataOutputStream outsend = new DataOutputStream(s.getOutputStream());
                        outsend.writeInt(3); // type
                        outsend.writeInt(userNameBuffer.length); //usernamelen
                        outsend.write(userNameBuffer, 0, userNameBuffer.length);
                        outsend.writeInt(file.getName().length()); //fileName
                        outsend.write(file.getName().getBytes());

                        long size = file.length();
                        outsend.writeLong(size);
                        byte[] buffer = new byte[1024];
                        System.out.printf("Sending loaded file to all subscribers %s (%d)", file.getName(), size);
                        while (size > 0) {
                            int len = insend.read(buffer, 0, buffer.length);
                            outsend.write(buffer, 0, len);
                            size -= len;

                        }
                        outsend.flush();
                        insend.close();
                    } catch (IOException ex) {
                        logger.info("Load my file error Internal server");
                        ex.printStackTrace();
                    }

                }
            }
        } catch (Exception e) {
            logger.info("Load my file error Internal server");
        }

    }

    public void sendMyImageMessage(File file) {
        String username = UserNameInput.getUserName();
        synchronized (subscribers) {

            for (int i = 0; i < subscribers.size(); i++) {
                try {
                    FileInputStream insend = new FileInputStream(file);
                    Socket s = subscribers.get(i);
                    DataOutputStream outsend = new DataOutputStream(s.getOutputStream());
                    byte[] buffer = new byte[1024];
                    outsend.writeInt(5); // type
                    outsend.writeInt(username.length()); //usernamelen
                    outsend.write(username.getBytes(), 0, username.length()); //username
                    outsend.writeInt(file.getName().length()); //filename
                    outsend.write(file.getName().getBytes()); //filename

                    long size = file.length();
                    outsend.writeLong(size);

                    System.out.printf("Uploading image %s (%d)", file.getName(), size);
                    while (size > 0) {
                        int len = insend.read(buffer, 0, buffer.length);
                        outsend.write(buffer, 0, len);
                        size -= len;
                    }
                    outsend.flush();
                    insend.close();
                } catch (IOException ex) {
                    logger.info("Client already disconnected");
                    ex.printStackTrace();
                }

            }
        }
    }

    public void sendMyFileMessage(File file) {
        String username = UserNameInput.getUserName();
            synchronized (subscribers) {
                for (int i = 0; i < subscribers.size(); i++) {
                    try {
                        Socket s = subscribers.get(i);
                        DataOutputStream out = new DataOutputStream(s.getOutputStream());
                        FileInputStream in = new FileInputStream(file);
                        byte[] buffer = new byte[1024];

                        out.writeInt(4); // type

                        out.writeInt(username.length());
                        out.write(username.getBytes(), 0, username.length());

                        out.writeInt(file.getName().length());
                        out.write(file.getName().getBytes());

                        long size = file.length();
                        out.writeLong(size);

                        while (size > 0) {
                            int len = in.read(buffer, 0, buffer.length);
                            out.write(buffer, 0, len);
                            size -= len;
                        }
                        out.flush();
                        in.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

    }

    private void sendStateToNewJoiner(Socket clientSocket) {
        try {
            /**
             * send a message code 3
             */

            File f = new File("lateststate.kpt");
            UI.getInstance().saveDrawingToFile(f);


            String Username = UserNameInput.getUserName();
            byte userNameBuffer[] = Username.getBytes();

            FileInputStream inL = new FileInputStream(f);
            Socket s = clientSocket;
            DataOutputStream outsend = new DataOutputStream(s.getOutputStream());
            outsend.writeInt(3); // type
            outsend.writeInt(userNameBuffer.length); //usernamelen
            outsend.write(userNameBuffer, 0, userNameBuffer.length);

            outsend.writeInt(f.getName().length()); //filename
            outsend.write(f.getName().getBytes()); //filename

            long size = f.length();
            outsend.writeLong(size);

            byte[] buffer = new byte[1024];
            System.out.printf("Sending state to new joiner %s (%d)", f.getName(), size);
            while (size > 0) {
                int len = inL.read(buffer, 0, buffer.length);
                outsend.write(buffer, 0, len);
                size -= len;
            }
            outsend.flush();
            inL.close();

        } catch (Exception e) {

        }

    }

}

