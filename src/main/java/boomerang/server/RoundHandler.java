package boomerang.server;

import boomerang.cards.Card;
import boomerang.cards.CardPrinter;
import boomerang.cards.DrawDeck;
import boomerang.cards.PlayerDeck;
import boomerang.deckhandling.DrawDeckCardMovement;

import java.io.ObjectInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;

public class RoundHandler {
    private RoundHandler() {
        // Private constructor to prevent instantiation.
    }

    public static void notifyPlayersOfDraftStart(ArrayList<ClientData> clientData, ArrayList<DrawDeck> drawDecks, ArrayList<Player> players, boolean firstRound) {
        for (ClientData client : clientData) {
            String availableCardsData;
            String availableCards = "";
            String throwCardData;
            String allPlayerDecks;

            DrawDeck drawDeck = null;
            for (DrawDeck curDrawDeck : drawDecks) {
                if (curDrawDeck.getPlayerID() == client.getClientID()) {
                    drawDeck = curDrawDeck;
                }
            }

            throwCardData = CardPrinter.getPlayerThrowCardContents(players.get(client.getClientID()));
            allPlayerDecks = CardPrinter.getPlayerDeckContents(players);
            availableCardsData = CardPrinter.getDrawDeckContents(drawDeck);

            for (Card card : drawDeck.getCards()) {
                availableCards = availableCards + " *" + card.getSite();
            }

            MessageToClientSender.sendMessageToPlayer(client.getOutToClient(), throwCardData);
            MessageToClientSender.sendMessageToPlayer(client.getOutToClient(), allPlayerDecks);
            MessageToClientSender.sendMessageToPlayer(client.getOutToClient(), availableCardsData);

            String message = firstRound
                    ? "Pick a card to keep as throw card " + availableCards
                    : "Pick a card to keep " + availableCards;

            MessageToClientSender.sendMessageToPlayer(client.getOutToClient(), message);
        }
    }

    public static void notifyPlayersOfActivityPick(ArrayList<ClientData> clientData, ArrayList<Player> players, ArrayList<String> activities) {
        for (ClientData client : clientData) {
            String availableActivities = "";
            Player player = players.get(client.getClientID());

            ArrayList<String> result = new ArrayList<>();

            ArrayList<String> playerUsedActivities = player.getUsedActivities();
            for (String element : activities) {
                if (!playerUsedActivities.contains(element)) {
                    result.add(element);
                }
            }

            for (String activity : result) {
                availableActivities = availableActivities + " *" + activity;
            }

            MessageToClientSender.sendMessageToPlayer(client.getOutToClient(), "Pick an activity " + availableActivities);
        }
    }

    public static void rotateDrawDeck(ArrayList<DrawDeck> drawDecks) {
        int highestPlayerID = -1;

        for (DrawDeck drawDeck : drawDecks) {
            if (drawDeck.getPlayerID() > highestPlayerID) {
                highestPlayerID = drawDeck.getPlayerID();
            }
        }

        for (DrawDeck drawDeck : drawDecks) {
            int newPlayerID = drawDeck.getPlayerID() + 1;
            if (newPlayerID > highestPlayerID) {
                newPlayerID = 0;
            }
            drawDeck.setPlayerID(newPlayerID);
        }
    }

    public static void runDraft(ArrayList<ClientData> clientData, ArrayList<Player> players, ArrayList<DrawDeck> drawDecks, boolean firstRound) {
        notifyPlayersOfDraftStart(clientData, drawDecks, players, firstRound);
        ArrayList<String> messages = new ArrayList<>();
        ExecutorService threadpool = Executors.newFixedThreadPool(clientData.size());

        executePlayerTasks(clientData, threadpool, messages);
        waitUntilAllTasksComplete(threadpool);
        processPlayerDrawMessages(messages, drawDecks, players);
        rotateDrawDeck(drawDecks);
        System.out.println("Draft finished\n");
    }

    public static void runActivityPick(ArrayList<ClientData> clientData, ArrayList<Player> players, ArrayList<String> activities) {
        notifyPlayersOfActivityPick(clientData, players, activities);
        ArrayList<String> messages = new ArrayList<>();
        ExecutorService threadpool = Executors.newFixedThreadPool(clientData.size());

        executePlayerTasks(clientData, threadpool, messages);
        waitUntilAllTasksComplete(threadpool);
        processPlayerActivityMessages(messages, players);
        System.out.println("Activities picks finished\n");
    }

    private static void executePlayerTasks(ArrayList<ClientData> clientData, ExecutorService threadpool, ArrayList<String> messages) {
        for (int p = 0; p < clientData.size(); p++) {
            final int currentPlayerIndex = p;
            Runnable task = createPlayerTask(clientData.get(currentPlayerIndex), messages);
            threadpool.execute(task);
        }
        threadpool.shutdown();
    }

    private static Runnable createPlayerTask(ClientData client, ArrayList<String> messages) {
        return new Runnable() {
            @Override
            public void run() {
                ObjectInputStream inFromClient = client.getInFromClient();
                String message = MessageFromClientReceiver.waitForMessage(inFromClient);
                messages.add(message + Integer.toString(client.getClientID()));
                System.out.print("Player " + client.getClientID() + " picked: " + message + "\n");
            }
        };
    }

    private static void waitUntilAllTasksComplete(ExecutorService threadpool) {
        while (!threadpool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void processPlayerDrawMessages(ArrayList<String> messages, ArrayList<DrawDeck> drawDecks, ArrayList<Player> players) {
        for (int i = 0; i < messages.size(); i++) {
            String message = messages.get(i);
            DrawDeck moveFromDrawDeck = findDrawDeckForMessage(drawDecks, message);
            DrawDeckCardMovement drawDeckCardMovement = new DrawDeckCardMovement();
            Player player = players.get(Character.getNumericValue(message.charAt(1)));
            PlayerDeck moveToPlayerDeck = player.getPlayerDeck();
            String site = String.valueOf(message.charAt(0));
            player.setTouristSiteVisited(site);
            drawDeckCardMovement.moveCard(moveFromDrawDeck, moveToPlayerDeck, site);
        }
    }

    private static void processPlayerActivityMessages(ArrayList<String> messages, ArrayList<Player> players) {
        for (int i = 0; i < messages.size(); i++) {
            String message = messages.get(i);
            Player player = players.get(Character.getNumericValue(message.charAt(message.length() - 1)));
            if (!message.substring(0, message.length() - 1).equals("No activity")) {
                player.addUsedActivity(message.substring(0, message.length() - 1));
            }
            player.setMostRecentActivity(message.substring(0, message.length() - 1));
        }
    }

    private static DrawDeck findDrawDeckForMessage(ArrayList<DrawDeck> drawDecks, String message) {
        for (DrawDeck drawDeck : drawDecks) {
            for (Card card : drawDeck.getCards()) {
                if (card.getSite().equals(String.valueOf(message.charAt(0)))){
                    return drawDeck;
                }
            }
        }
        return null; // Handle the case where no draw deck is found
    }
}
