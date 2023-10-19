package boomerang.server;

import boomerang.server.gameboard.ClientData;
import boomerang.server.gameboard.GameBoard;
import boomerang.server.gameboard.Player;

import java.io.*;
import java.net.*;

public class GameServer {
    private int numberPlayers;
    private GameBoard gameBoard;
    public GameServer(int numberPlayers, String country){
        this.numberPlayers = numberPlayers;
        this.gameBoard = new GameBoard(country);
    }

    //waits for players to connect and adds data to the gameboard
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

                outToClient.writeObject("You are player: " + i);
                System.out.println("Player" + i + " joined" + "\n");
            }
        }
        catch (IOException e) {
           System.out.println("error");
        }
        gameBoard.runner();
    }
}