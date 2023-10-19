package boomerang.server.gameboard;

import java.io.*;
import java.net.*;

//keeps track of the networking for a player
public class ClientData {
    private int clientID;
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;

    public ClientData(int clientID, ObjectInputStream inFromClient, ObjectOutputStream outToClient){
        this.clientID = clientID;
        this.inFromClient = inFromClient;
        this.outToClient = outToClient;
    }

    public int getClientID(){
        return this.clientID;
    }
    public ObjectOutputStream getOutToClient(){return this.outToClient;}
    public ObjectInputStream getInFromClient(){return this.inFromClient;}
}
