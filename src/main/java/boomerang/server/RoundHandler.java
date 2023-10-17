package boomerang.server;

import java.io.ObjectInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;

public class RoundHandler {
    private ArrayList<ClientData> clientData;
    private ArrayList<String> testArray = new ArrayList<String>();
    public RoundHandler(ArrayList<ClientData> clientData) {
        this.clientData = clientData;
    }

    public void notifyPlayersOfRoundStart(){
        for(ClientData clientData : this.clientData) {
            MessageToClientSender messageToClientSender = new MessageToClientSender();
            //messageToClientSender.sendMessageToPlayers(clientData.outToClient,) skicka data om korten

            messageToClientSender.sendMessageToPlayers(clientData.outToClient, "Pick a card to keep ");
        }
    }

    public void startRound(ArrayList<Player> players) {
        notifyPlayersOfRoundStart(

        );
        ExecutorService threadpool = Executors.newFixedThreadPool(clientData.size());
        for (int p = 0; p < clientData.size(); p++) {
            final int currentPlayerIndex = p;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    ObjectInputStream inFromClient = clientData.get(currentPlayerIndex).inFromClient;
                    MessageFromClientReceiver messageFromClient = new MessageFromClientReceiver(inFromClient);

                    String message = messageFromClient.waitForMessage();
                    System.out.print("Player " + currentPlayerIndex + " picked: " + message + "\n");

                     //players.get(currentPlayerIndex).getPlayerDeck().drawCard(card);
                    System.out.println(testArray);
                }
            };
            threadpool.execute(task);
        }
        threadpool.shutdown();

        while (!threadpool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Round finished\n");
    }
}
