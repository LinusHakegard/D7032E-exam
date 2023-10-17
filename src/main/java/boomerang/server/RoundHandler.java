package boomerang.server;

import boomerang.cards.Card;
import boomerang.cards.DrawDeck;
import boomerang.cards.PlayerDeck;
import boomerang.deckhandling.DrawDeckCardMovement;

import java.io.ObjectInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;


public class RoundHandler {
    private ArrayList<ClientData> clientData;

    public RoundHandler(ArrayList<ClientData> clientData) {
        this.clientData = clientData;
    }

    public void notifyPlayersOfRoundStart(ArrayList<DrawDeck> drawDecks){

        for(ClientData clientData : this.clientData) {
            String availableCards = "";
            MessageToClientSender messageToClientSender = new MessageToClientSender();
            //messageToClientSender.sendMessageToPlayers(clientData.outToClient,) skicka data om korten

            DrawDeck drawDeck = null;
            for(DrawDeck curDrawDeck : drawDecks){
                if(curDrawDeck.getPlayerID() == clientData.clientID){
                    drawDeck = curDrawDeck;
                }
            }

            for(Card card : drawDeck.getCards()){
                availableCards = availableCards + " *" + card.getSite();
            }

            messageToClientSender.sendMessageToPlayers(clientData.outToClient, "Pick a card to keep " + availableCards);
        }
    }

    public void startRound(ArrayList<Player> players, ArrayList<DrawDeck> drawDecks) {
        notifyPlayersOfRoundStart(drawDecks);
        ExecutorService threadpool = Executors.newFixedThreadPool(clientData.size());
        for (int p = 0; p < clientData.size(); p++) {
            final int currentPlayerIndex = p;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    ObjectInputStream inFromClient = clientData.get(currentPlayerIndex).inFromClient;
                    MessageFromClientReceiver messageFromClient = new MessageFromClientReceiver(inFromClient);

                    String message = messageFromClient.waitForMessage();


                    Player player = players.get(currentPlayerIndex);
                    PlayerDeck playerDeck = player.getPlayerDeck();
                    DrawDeck drawDeck = null;
                    for(DrawDeck curDrawDeck : drawDecks){
                        if(curDrawDeck.getPlayerID() == player.getPlayerID()){
                            drawDeck = curDrawDeck;
                        }
                    }
                    for(Card card : drawDecks.get(currentPlayerIndex).getCards()){
                        if(card.getSite().equals(message)){
                            DrawDeckCardMovement drawDeckCardMovement = new DrawDeckCardMovement();
                            drawDeckCardMovement.moveCard(drawDeck, playerDeck, card.getSite());
                        }

                    }
                    System.out.print("Player " + currentPlayerIndex + " picked: " + message + "\n");
                    for (Card card : player.getPlayerDeck().getCards()) {
                        System.out.println("Name: " + card.getName());
                        System.out.println("Number: " + card.getNumber());
                        System.out.println("Site: " + card.getSite());
                        System.out.println("Region: " + card.getRegion());
                        System.out.println("Collection: " + card.getCollection());
                        System.out.println("Animal: " + card.getAnimal());
                        System.out.println("Activity: " + card.getActivity());
                        System.out.println();
                    }
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
