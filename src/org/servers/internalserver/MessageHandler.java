package org.servers.internalserver;

import org.gui.UI;
import org.gui.chat.ChatArea;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class MessageHandler {
    private static Logger logger = Logger.getLogger("global");

    private MessageHandler(){}

    protected static void handleChatMessage(DataInputStream in, ArrayList<Socket> subscribers) throws IOException {
        byte[] userNameBuffer = new byte[1024];
        int userNameLen = in.readInt();
        in.read(userNameBuffer, 0, userNameLen);

        byte[] chatBuffer = new byte[1024];
        int chatLen = in.readInt();
        in.read(chatBuffer, 0, chatLen);

        ChatArea.getInstance().addMessage(new String(chatBuffer, 0, chatLen), new String(userNameBuffer, 0, userNameLen));
        synchronized (subscribers) {
            logger.info("" + subscribers.size() + "Subscribers");
            for (int i = 0; i < subscribers.size(); i++) {
                logger.info("Sending data to sub: " + subscribers.get(i));
                try {
                    Socket s = subscribers.get(i);
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());
                    out.writeInt(0);//type
                    out.writeInt(userNameLen);
                    out.write(userNameBuffer, 0, userNameLen);
                    out.writeInt(chatLen);
                    out.write(chatBuffer, 0, chatLen);
                    out.flush();
                } catch (IOException ex) {
                    logger.info("Client already disconnected");
                }

            }
        }
    }

    protected static void handlePixelMessage(DataInputStream in, ArrayList<Socket> subscribers) throws IOException {
        int color = in.readInt();
        int x = in.readInt();
        int y = in.readInt();
        int pentype = in.readInt();
        int pensize = in.readInt();

        UI.getInstance().paintPixel(color, x, y, pentype, pensize);
        synchronized (subscribers) {
            for (int i = 0; i < subscribers.size(); i++) {
                try {
                    Socket s = subscribers.get(i);
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());
                    out.writeInt(1); // type
                    out.writeInt(color);
                    out.writeInt(x);
                    out.writeInt(y);
                    out.writeInt(pentype);
                    out.writeInt(pensize);
                    out.flush();
                } catch (IOException ex) {
                    logger.info("Client already disconnected");
                    ex.printStackTrace();
                }

            }
        }
    }

    protected static void handleBucketMessage(DataInputStream in, ArrayList<Socket> subscribers) throws IOException {
        int color = in.readInt();
        int x = in.readInt();
        int y = in.readInt();
        UI.getInstance().paintArea(color, x, y);

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
    }

    protected static void handleDataLoadMessage(DataInputStream in, ArrayList<Socket> subscribers) throws IOException {
        byte[] userNameBuffer = new byte[1024];
        int userNameLen = in.readInt();
        in.read(userNameBuffer, 0, userNameLen);

        byte[] buffer = new byte[1024];
        int fileNameSize = in.readInt();

        in.read(buffer, 0, fileNameSize);
        String fileName = new String(buffer, 0, fileNameSize);
        logger.info("Downloading File");


        long fileSize = in.readLong();

        File file = new File(fileName);
        FileOutputStream out = new FileOutputStream(file);

        while (fileSize > 0) {
            int len = in.read(buffer, 0, buffer.length);
            out.write(buffer, 0, len);
            fileSize = fileSize - len;
        }

        //loaded file into File


        int[][][] output = new int[2][100][100];

        int[][][] outputDataArray = new int[1][][];
        boolean[] success = new boolean[1];
        int[] outputBlockSize = new int[1];
        UI.loadDrawingFromFileIntoDataArray(file, outputDataArray, outputBlockSize, success);
        if (!success[0]) {
            logger.info("Loading failed at server: unable to properly load the file sent by the client");
            return;
        }
        //update the server's own data array
        UI.getInstance().loadDrawingLocally(outputDataArray[0], outputBlockSize[0]);

//        send this new data to all subscribers
        synchronized (subscribers) {
            logger.info("" + subscribers.size() + "Subscribers");
            for (int i = 0; i < subscribers.size(); i++) {
                try {
                    FileInputStream insend = new FileInputStream(file);
                    Socket s = subscribers.get(i);
                    DataOutputStream outsend = new DataOutputStream(s.getOutputStream());
                    outsend.writeInt(3); // type
                    outsend.writeInt(userNameLen); //usernamelen
                    outsend.write(userNameBuffer, 0, userNameLen);
                    outsend.writeInt(file.getName().length()); //fileName
                    outsend.write(file.getName().getBytes());

                    long size = file.length();
                    outsend.writeLong(size);

                    System.out.printf("Sending loaded file to all subscribers %s (%d)", file.getName(), size);
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

    protected static void handleFileMessage(DataInputStream in, ArrayList<Socket> subscribers) throws IOException {

        byte[] userNameBuffer = new byte[1024];
        int userNameLen = in.readInt();
        in.read(userNameBuffer, 0, userNameLen);

        byte[] buffer = new byte[1024];
        int fileNameSize = in.readInt();

        in.read(buffer, 0, fileNameSize);
        String fileName = new String(buffer, 0, fileNameSize);
        logger.info("Downloading File"+ fileName);


        long fileSize = in.readLong();

        File outputFile = new File(fileName);
        FileOutputStream out = new FileOutputStream(outputFile);

        while (fileSize > 0) {
            int len = in.read(buffer, 0, buffer.length);
            out.write(buffer, 0, len);
            fileSize = fileSize - len;
        }
        logger.info("Download complete");
        out.close();


        ChatArea.getInstance().addFileMessage(outputFile, new String(userNameBuffer,0,userNameLen));

        File f = new File(fileName);

        synchronized (subscribers) {

            for (int i = 0; i < subscribers.size(); i++) {
                try {
                    FileInputStream insend = new FileInputStream(f);
                    Socket s = subscribers.get(i);
                    DataOutputStream outsend = new DataOutputStream(s.getOutputStream());
                    outsend.writeInt(4); // type
                    outsend.writeInt(userNameLen); //usernamelen
                    outsend.write(userNameBuffer, 0, userNameLen);
                    outsend.writeInt(f.getName().length()); //fileName
                    outsend.write(f.getName().getBytes());

                    long size = f.length();
                    outsend.writeLong(size);

                    System.out.printf("Uploading file %s (%d)", f.getName(), size);
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

    protected static void handleImageMessage(DataInputStream in, ArrayList<Socket> subscribers) throws IOException {

        byte[] userNameBuffer = new byte[1024];
        int userNameLen = in.readInt();
        in.read(userNameBuffer, 0, userNameLen);
        String username = new String(userNameBuffer, 0, userNameLen);

        byte[] buffer = new byte[1024];
        int imageNameSize = in.readInt();

        in.read(buffer, 0, imageNameSize);
        String imageName = new String(buffer, 0, imageNameSize);
        logger.info("Downloading Image");


        long imageSize = in.readLong();

        File outputImage = new File(imageName);
        FileOutputStream out = new FileOutputStream(outputImage);

        while (imageSize > 0) {
            int len = in.read(buffer, 0, buffer.length);
            out.write(buffer, 0, len);
            imageSize = imageSize - len;
        }
        logger.info("Download complete");
        out.close();

        ChatArea.getInstance().addImageMessage(outputImage, username);


        File f = new File(imageName);

        synchronized (subscribers) {

            for (int i = 0; i < subscribers.size(); i++) {
                try {
                    FileInputStream insend = new FileInputStream(f);
                    Socket s = subscribers.get(i);
                    DataOutputStream outsend = new DataOutputStream(s.getOutputStream());
                    outsend.writeInt(5); // type
                    outsend.writeInt(userNameLen); //usernamelen
                    outsend.write(userNameBuffer, 0, userNameLen); //username
                    outsend.writeInt(f.getName().length()); //filename
                    outsend.write(f.getName().getBytes()); //filename

                    long size = f.length();
                    outsend.writeLong(size);

                    System.out.printf("Uploading image %s (%d)", f.getName(), size);
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
}
