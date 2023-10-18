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

    public void notifyPlayersOfDraftStart(ArrayList<DrawDeck> drawDecks, ArrayList<Player> players, boolean firstRound){

        for(ClientData clientData : this.clientData) {
            String availableCardsData ;
            String availableCards = "";
            String throwCardData ;
            String allPlayerDecks;

            MessageToClientSender messageToClientSender = new MessageToClientSender();

            DrawDeck drawDeck = null;
            for(DrawDeck curDrawDeck : drawDecks){
                if(curDrawDeck.getPlayerID() == clientData.clientID){
                    drawDeck = curDrawDeck;
                }
            }

            throwCardData = getPlayerThrowCardContents(players.get(clientData.getClientID()));
            allPlayerDecks = getPlayerDeckContents(players);
            availableCardsData = getDrawDeckContents(drawDeck);

            for(Card card : drawDeck.getCards()){
                availableCards = availableCards + " *" + card.getSite();
            }

            messageToClientSender.sendMessageToPlayers(clientData.outToClient, throwCardData);
            messageToClientSender.sendMessageToPlayers(clientData.outToClient, allPlayerDecks);
            messageToClientSender.sendMessageToPlayers(clientData.outToClient, availableCardsData);

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

        notifyPlayersOfDraftStart(drawDecks, players, firstRound);

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

            player.setTouristSiteVisited(site);
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

    //ska göras direkt i deck klasserna
    private String getPlayerDeckContents(ArrayList<Player> players) {
        StringBuilder result = new StringBuilder();

        for (Player player : players) {
            result.append("Player " + player.getPlayerID() + "'s cards (excluding throw card):").append("\n");
            ArrayList<Card> playerCards = player.getPlayerDeck().getCards();

            for (int i = 1; i < playerCards.size(); i++) { // Start from index 1 to skip the first card
                Card card = playerCards.get(i);
                result.append("Name: ").append(card.getName())
                        .append(" | Number: ").append(card.getNumber())
                        .append(" | Site: ").append(card.getSite())
                        .append(" | Region: ").append(card.getRegion())
                        .append(" | Collection: ").append(card.getCollection())
                        .append(" | Animal: ").append(card.getAnimal())
                        .append(" | Activity: ").append(card.getActivity())
                        .append("\n");
            }

            result.append("\n");
        }

        return result.toString();
    }

    private String getDrawDeckContents(DrawDeck drawDeck) {
        StringBuilder result = new StringBuilder();


        result.append("Available cards: ").append("\n");
        ArrayList<Card> cards = drawDeck.getCards();


        for (int i = 0; i < drawDeck.getCards().size(); i++) {
            Card card = cards.get(i);
            result.append("Name: ").append(card.getName())
                    .append(" | Number: ").append(card.getNumber())
                    .append(" | Site: ").append(card.getSite())
                    .append(" | Region: ").append(card.getRegion())
                    .append(" | Collection: ").append(card.getCollection())
                    .append(" | Animal: ").append(card.getAnimal())
                    .append(" | Activity: ").append(card.getActivity())
                    .append("\n");
        }

            result.append("\n");


        return result.toString();
    }

    private String getPlayerThrowCardContents(Player player) {
        StringBuilder result = new StringBuilder();
        Card card = player.getPlayerDeck().getFirstCard();

        result.append("Your throw card is: ").append("\n");
        if(card != null) {
            result.append("Name: ").append(card.getName())
                    .append(" | Number: ").append(card.getNumber())
                    .append(" | Site: ").append(card.getSite())
                    .append(" | Region: ").append(card.getRegion())
                    .append(" | Collection: ").append(card.getCollection())
                    .append(" | Animal: ").append(card.getAnimal())
                    .append(" | Activity: ").append(card.getActivity())
                    .append("\n");

            result.append("\n");
        }


        return result.toString();
    }
}
