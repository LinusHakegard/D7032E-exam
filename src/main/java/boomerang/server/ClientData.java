package boomerang.server;

import java.io.*;
import java.net.*;

public class ClientData {
    private final int clientID;
    private final ObjectInputStream inFromClient;
    private final ObjectOutputStream outToClient;

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
