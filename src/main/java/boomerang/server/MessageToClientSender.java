package boomerang.server;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class MessageToClientSender {


    public static void sendMessageToPlayer(ObjectOutputStream outToClient, String message){
        try{
            outToClient.writeObject(message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
