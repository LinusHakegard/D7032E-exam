package boomerang.server;

import boomerang.server.gameboard.ClientData;
import boomerang.server.gameboard.GameBoard;
import boomerang.server.gameboard.Player;

import java.io.*;
import java.net.*;

public class GameServer {
    int numberPlayers;
    GameBoard gameBoard;
    public GameServer(int numberPlayers, String country){
        this.numberPlayers = numberPlayers;
        this.gameBoard = new GameBoard(country);
    }
    public void waitForPlayers() throws Exception{
        final int PORT = 2048;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running and waiting for players...");

            for(int i = 0; i < this.numberPlayers; i++){
                Socket connectionSocket = serverSocket.accept();
                ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
                ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());

                this.gameBoard.addClient(new ClientData(i, inFromClient, outToClient));
                this.gameBoard.addPlayer(new Player(i));

                //fixa med message to client sender
                outToClient.writeObject("You are player: " + i);
                System.out.println("Player" + i + " joined" + "\n");



                //tillfälliga test grejer
                /*outToClient.writeObject("Yo bro");
                Scanner in = new Scanner(System.in);
                outToClient.writeObject(in.nextLine());*/
            }
        }
        catch (IOException e) {
           System.out.println("oops server");
        }
        gameBoard.runner();
    }
}