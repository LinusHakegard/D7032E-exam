package boomerang.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MessageToClientSender {


    public void sendMessageToPlayers(ObjectOutputStream outToClient, String message){
        try{
            outToClient.writeObject(message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
