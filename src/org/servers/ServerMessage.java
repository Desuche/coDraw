package org.servers;

public enum ServerMessage {
    ChatMessage(0),
    PixelMessage(1),
    BucketMessage(2),
    DataLoadMessage(3),
    FileMessage(4),
    ImageMessage(5),
    UnsupportedMessage(-1);

    public final int value;

    ServerMessage(int value){
        this.value = value;
    }

    public static ServerMessage getServerMessage(int value){
        switch (value){
            case 0:
                return ChatMessage;
            case 1:
                return PixelMessage;
            case 2:
                return BucketMessage;
            case 3:
                return DataLoadMessage;
            case 4:
                return FileMessage;
            case 5:
                return ImageMessage;
            default:
                return UnsupportedMessage;

        }
    }
}
