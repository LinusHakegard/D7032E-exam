package boomerang.server;

import boomerang.cards.Card;
import boomerang.cards.DrawDeck;
import boomerang.cards.PlayerDeck;
import boomerang.deckhandling.DrawDeckCardMovement;

import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;


public class RoundHandler {
    private ArrayList<ClientData> clientData;

    public RoundHandler(ArrayList<ClientData> clientData) {
        this.clientData = clientData;
    }

    public void notifyPlayersOfDraftStart(ArrayList<DrawDeck> drawDecks, boolean firstRound){

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

            if(firstRound){
                messageToClientSender.sendMessageToPlayers(clientData.outToClient, "Pick a card to keep as throw card " + availableCards);
            }
            else{
                messageToClientSender.sendMessageToPlayers(clientData.outToClient, "Pick a card to keep " + availableCards);

            }
        }
    }
    public void notifyPlayersOfActivityPick(ArrayList<Player> players, ArrayList<String> activities){
        for(ClientData clientData : this.clientData) {
            String availableActivities = "";
            MessageToClientSender messageToClientSender = new MessageToClientSender();

            Player player = players.get(clientData.getClientID());

            ArrayList<String> result = new ArrayList<>();

            ArrayList<String> playerUsedActivities = player.getUsedActivities();
            for (String element : activities) {
                if (!playerUsedActivities.contains(element)) {
                    result.add(element);
                }
            }

            for(String activity : result){
                availableActivities = availableActivities + " *" + activity;
            }

            messageToClientSender.sendMessageToPlayers(clientData.outToClient, "Pick an activity " + availableActivities);

        }
    }
    public void rotateDrawDeck(ArrayList<DrawDeck> drawDecks){
        int highestPlayerID = -1;

        // Find the highest playerID
        for (DrawDeck drawDeck : drawDecks) {
            if (drawDeck.getPlayerID() > highestPlayerID) {
                highestPlayerID = drawDeck.getPlayerID();
            }
        }

        // Increment playerIDs by 1 and wrap around if needed
        for (DrawDeck drawDeck : drawDecks) {
            int newPlayerID = drawDeck.getPlayerID() + 1;

            // Check if newPlayerID exceeds the highest, reset to 0
            if (newPlayerID > highestPlayerID) {
                newPlayerID = 0;
            }

            drawDeck.setPlayerID(newPlayerID);
        }
    }


    public void runDraft(ArrayList<Player> players, ArrayList<DrawDeck> drawDecks, boolean firstRound) {

        notifyPlayersOfDraftStart(drawDecks, firstRound);

        ArrayList<String> messages = new ArrayList<>();
        ExecutorService threadpool = Executors.newFixedThreadPool(clientData.size());

        executePlayerTasks(threadpool, messages);
        waitUntilAllTasksComplete(threadpool);
        processPlayerDrawMessages(messages, drawDecks, players);
        rotateDrawDeck(drawDecks);
        System.out.println("Draft finished\n");
    }

   public void runActivityPick(ArrayList<Player> players, ArrayList<String> activities) {
        notifyPlayersOfActivityPick(players, activities);

        ArrayList<String> messages = new ArrayList<>();
        ExecutorService threadpool = Executors.newFixedThreadPool(clientData.size());

        executePlayerTasks(threadpool, messages);
        waitUntilAllTasksComplete(threadpool);
        processPlayerActivityMessages(messages, players);

        System.out.println("Activities picks finished\n");
    }

    private void executePlayerTasks(ExecutorService threadpool, ArrayList<String> messages) {
        for (int p = 0; p < clientData.size(); p++) {
            final int currentPlayerIndex = p;
            Runnable task = createPlayerTask(currentPlayerIndex, messages);
            threadpool.execute(task);
        }
        threadpool.shutdown();
    }

    private Runnable createPlayerTask(int currentPlayerIndex, ArrayList<String> messages) {
        return new Runnable() {
            @Override
            public void run() {
                ObjectInputStream inFromClient = clientData.get(currentPlayerIndex).inFromClient;
                MessageFromClientReceiver messageFromClient = new MessageFromClientReceiver(inFromClient);

                String message = messageFromClient.waitForMessage();
                messages.add(message + Integer.toString(currentPlayerIndex));
                System.out.print("Player " + currentPlayerIndex + " picked: " + message + "\n");
            }
        };
    }

    private void waitUntilAllTasksComplete(ExecutorService threadpool) {
        while (!threadpool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processPlayerDrawMessages(ArrayList<String> messages, ArrayList<DrawDeck> drawDecks, ArrayList<Player> players) {
        for (int i = 0; i < messages.size(); i++) {
            String message = messages.get(i);
            System.out.println(message);

            DrawDeck moveFromDrawDeck = findDrawDeckForMessage(drawDecks, message);

            DrawDeckCardMovement drawDeckCardMovement = new DrawDeckCardMovement();
            Player player = players.get(Character.getNumericValue(message.charAt(1)));
            PlayerDeck moveToPlayerDeck = player.getPlayerDeck();
            String site = String.valueOf(message.charAt(0));

            drawDeckCardMovement.moveCard(moveFromDrawDeck, moveToPlayerDeck, site);
        }
    }

    private void processPlayerActivityMessages(ArrayList<String> messages, ArrayList<Player> players) {
        for (int i = 0; i < messages.size(); i++) {
            String message = messages.get(i);
            System.out.println(message);
            Player player = players.get(Character.getNumericValue(message.charAt(message.length() - 1)));
            if(!message.substring(0, message.length() - 1).equals("No activity")){
                player.addUsedActivity(message.substring(0, message.length() - 1));
            }
            player.setMostRecentActivity(message.substring(0, message.length() - 1));

            System.out.println(player.getUsedActivities());
        }
    }

    private DrawDeck findDrawDeckForMessage(ArrayList<DrawDeck> drawDecks, String message) {
        for (DrawDeck drawDeck : drawDecks) {
            for (Card card : drawDeck.getCards()) {
                if (card.getSite().equals(String.valueOf(message.charAt(0)))){
                    return drawDeck;
                }
            }
        }
        return null; // Handle the case where no draw deck is found
    }

    private void displayPlayerDeckContents(ArrayList<Player> players) {
        for (Card card : players.get(0).getPlayerDeck().getCards()) {
            System.out.println("Name: " + card.getName());
            System.out.println("Number: " + card.getNumber());
            System.out.println("Site: " + card.getSite());
            System.out.println("Region: " + card.getRegion());
            System.out.println("Collection: " + card.getCollection());
            System.out.println("Animal: " + card.getAnimal());
            System.out.println("Activity: " + card.getActivity());


        }
    }
}
