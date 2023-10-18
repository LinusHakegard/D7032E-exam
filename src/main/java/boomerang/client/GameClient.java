package boomerang.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class GameClient {
    String serverIP;

    public GameClient(String serverIP){
        this.serverIP = serverIP;
    }
    public void run() throws IOException {
        final int SERVER_PORT = 2048;

        try {
            Socket socket = new Socket(this.serverIP, SERVER_PORT);
            ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());

            System.out.println("GameClient started");

            String nextMessage = "";
            while(!nextMessage.contains("winner")){
                nextMessage = (String) inFromServer.readObject();
                System.out.println(nextMessage);

                if(nextMessage.contains("Pick") || nextMessage.contains("keep")) {

                    Scanner in = new Scanner(System.in);
                    String inputLine = "MESSAGE WILL NOT CONTAIN THIS";
                    while(!nextMessage.contains("*"+inputLine)){
                        if(inputLine != "MESSAGE WILL NOT CONTAIN THIS"){
                            System.out.println("Invalid Input");
                        }
                        inputLine = in.nextLine();
                    }
                    outToServer.writeObject(inputLine);
                }
            }

        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println("oops client");
        }
    }

    }
