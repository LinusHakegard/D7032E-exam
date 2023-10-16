package boomerang.server;

import java.io.ObjectInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;

public class RoundHandler {
    private ArrayList<ClientData> clientData;

    public RoundHandler(ArrayList<ClientData> clientData) {
        this.clientData = clientData;
    }

    public void notifyPlayersOfRoundStart(){
        MessageToClientsSender messageToClientsSender = new MessageToClientsSender(clientData);
        messageToClientsSender.sendMessageToPlayers("Pick a card to keep");
    }

    public void startRound() {
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
