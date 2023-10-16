package boomerang.server;

import java.io.IOException;
import java.util.ArrayList;

public class MessageToClientsSender {
    private ArrayList<ClientData> clientData;
    public MessageToClientsSender(ArrayList<ClientData> clientData) {
        this.clientData = clientData;
    }
    public void sendMessageToPlayers(String message){
        try{
            for(ClientData clientData : this.clientData){
                clientData.outToClient.writeObject(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
