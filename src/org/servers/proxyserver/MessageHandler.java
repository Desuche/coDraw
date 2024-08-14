package org.servers.proxyserver;

import org.gui.UI;
import org.gui.chat.ChatArea;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class MessageHandler {
    private static Logger logger = Logger.getLogger("global");
    private MessageHandler(){}

    protected static void handleChatMessage(DataInputStream in) throws IOException{
        int usernameLength = in.readInt();
        byte[] b = new byte[usernameLength];
        in.read(b, 0, usernameLength);
        String incomingUsername = new String(b, 0, usernameLength);
        int messageLength = in.readInt();
        b = new byte[messageLength];
        in.read(b, 0, messageLength);
        String incomingMessage = new String(b, 0, messageLength);
        ChatArea.getInstance().addMessage(incomingMessage, incomingUsername);
    }
    protected static void handlePixelMessage(DataInputStream in) throws IOException {
        int pixelColor = in.readInt();
        int pixelX = in.readInt();
        int pixelY = in.readInt();
        int penType = in.readInt();
        int penSize = in.readInt();
        UI.getInstance().paintPixel(pixelColor, pixelX, pixelY,penType,penSize);
    }

    protected static void handleBucketMessage(DataInputStream in) throws IOException{
        int bucketColor = in.readInt();
        int bucketX = in.readInt();
        int bucketY = in.readInt();
        UI.getInstance().paintArea(bucketColor, bucketX, bucketY);
    }

    protected static void handleImageMessage(DataInputStream in) throws IOException{
        int usernameLength1Image = in.readInt();
        byte[] bbb = new byte[usernameLength1Image];
        in.read(bbb, 0, usernameLength1Image);
        String incomingUsername2 = new String(bbb, 0, usernameLength1Image);

        byte[] buffers = new byte[1024];
        int imageNameSize = in.readInt();

        in.read(buffers,0,imageNameSize);
        String imageName= new String(buffers,0,imageNameSize);
        while (new File(imageName).exists()){
            imageName = "_" + imageName;
        }
        logger.info("Downloading Image");
        File outputImage = new File(imageName);
        FileOutputStream outImage = new FileOutputStream(outputImage);

        long imageSize = in.readLong();


        while (imageSize>0)
        {
            int len=in.read(buffers,0, buffers.length);
            outImage.write(buffers,0,len);
            imageSize=imageSize-len;
        }
        outImage.flush();
        outImage.close();
        logger.info("Download complete");

        ChatArea.getInstance().addImageMessage(outputImage, incomingUsername2);
    }

    protected static void handleFileMessage(DataInputStream in) throws IOException{
        int usernameLength1File = in.readInt();
        byte[] bb = new byte[usernameLength1File];
        in.read(bb, 0, usernameLength1File);
        String incomingUsername1 = new String(bb, 0, usernameLength1File);

        byte[] buffer = new byte[1024];
        int fileNameSize = in.readInt();

        in.read(buffer,0,fileNameSize);
        String fileName= new String(buffer,0,fileNameSize);
        while (new File(fileName).exists()){
            fileName = "_" + fileName;
        }
        logger.info("Downloading File from server "+ fileName);
        File outputFile = new File(fileName);
        FileOutputStream outFile = new FileOutputStream(outputFile);

        long fileSize = in.readLong();



        while (fileSize>0)
        {
            int len=in.read(buffer,0, buffer.length);
            outFile.write(buffer,0,len);
            fileSize=fileSize-len;
        }
        outFile.flush();
        outFile.close();

        ChatArea.getInstance().addFileMessage(outputFile, incomingUsername1);
    }

    protected static void handleDataLoadMessage(DataInputStream in) throws IOException{
        File file = readCompleteFileFromServer(in);
        int[][][] tempData = new int [1][1][1];
        int[] tempBlockSize = new int [1];
        boolean[] success = new boolean[1];
        UI.loadDrawingFromFileIntoDataArray(file, tempData, tempBlockSize, success);
        if (success[0]){
            UI.getInstance().loadDrawingLocally(tempData[0], tempBlockSize[0]);
        }
    }

    private static File readCompleteFileFromServer(DataInputStream in) {
        try {
            int usernameLength1File = in.readInt();
            byte[] bb = new byte[usernameLength1File];
            in.read(bb, 0, usernameLength1File);
            String incomingUsername1 = new String(bb, 0, usernameLength1File);

            byte[] buffer = new byte[1024];
            int fileNameSize = in.readInt();

            in.read(buffer, 0, fileNameSize);
            String fileName = new String(buffer, 0, fileNameSize);
            while (new File(fileName).exists()){
                fileName = "_" + fileName;
            }
            logger.info("Downloading File from server " + fileName);
            File outputFile = new File(fileName);
            FileOutputStream outFile = new FileOutputStream(outputFile);

            long fileSize = in.readLong();


            while (fileSize > 0) {
                int len = in.read(buffer, 0, buffer.length);
                outFile.write(buffer, 0, len);
                fileSize = fileSize - len;
            }
            outFile.flush();
            outFile.close();
            logger.info("File read complete");
            return outputFile;
        }
        catch (Exception e){
            logger.info("readCompleteFileFromServer()");
            return null;
        }

    }

}
