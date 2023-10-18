package boomerang.server;

import java.io.*;
import java.net.*;

public class ClientData {
    int clientID;
    Socket connectionSocket;
    ObjectInputStream inFromClient;
    ObjectOutputStream outToClient;

    public ClientData(int clientID, Socket connectionSocket, ObjectInputStream inFromClient, ObjectOutputStream outToClient){
        this.clientID = clientID;
        this.connectionSocket = connectionSocket;
        this.inFromClient = inFromClient;
        this.outToClient = outToClient;
    }

    public int getClientID(){
        return this.clientID;
    }
}
