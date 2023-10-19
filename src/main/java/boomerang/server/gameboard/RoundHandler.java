package boomerang.server.gameboard;

import boomerang.server.cards.Card;
import boomerang.server.cards.CardPrinter;
import boomerang.server.cards.DrawDeck;
import boomerang.server.cards.PlayerDeck;
import boomerang.server.deckhandling.DrawDeckCardMovement;
import boomerang.server.gameboard.ClientData;
import boomerang.server.gameboard.MessageFromClientReceiver;
import boomerang.server.gameboard.MessageToClientSender;
import boomerang.server.gameboard.Player;

import java.io.ObjectInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;

public class RoundHandler {
    private RoundHandler() {
        // Private constructor to prevent instantiation.
    }

    public static void notifyPlayersOfDraftStart(ArrayList<ClientData> clientData, ArrayList<Player> players) {
        for (ClientData client : clientData) {
            String availableCardsData;
            String availableCards = "";
            String throwCardData;
            String allPlayerDecks;

            Player player = players.get(client.getClientID());
            DrawDeck drawDeck = player.getDrawDeck();

            throwCardData = CardPrinter.getPlayerThrowCardContents(player);
            allPlayerDecks = CardPrinter.getPlayerDeckContents(players);
            availableCardsData = CardPrinter.getDrawDeckContents(drawDeck);

            for (Card card : drawDeck.getCards()) {
                availableCards = availableCards + " *" + card.getSite();
            }

            MessageToClientSender.sendMessageToPlayer(client.getOutToClient(), throwCardData);
            MessageToClientSender.sendMessageToPlayer(client.getOutToClient(), allPlayerDecks);
            MessageToClientSender.sendMessageToPlayer(client.getOutToClient(), availableCardsData);

            String message = "Pick a card to keep " + availableCards;

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

    public static void rotateDrawDeck(ArrayList<Player> players, boolean lastRotation) {
        int numPlayers = players.size();

        // Create a temporary array to store the draw decks
        DrawDeck[] tempDrawDecks = new DrawDeck[numPlayers];

        if (lastRotation) {
            // Rotate counterclockwise
            for (int i = 0; i < numPlayers; i++) {
                int newIndex = (i - 1 + numPlayers) % numPlayers; // Rotate counterclockwise
                tempDrawDecks[newIndex] = players.get(i).getDrawDeck();
            }
        } else {
            // Rotate clockwise
            for (int i = 0; i < numPlayers; i++) {
                int newIndex = (i + 1) % numPlayers; // Rotate clockwise
                tempDrawDecks[newIndex] = players.get(i).getDrawDeck();
            }
        }

        // Update each player's draw deck based on the temporary array
        for (int i = 0; i < numPlayers; i++) {
            players.get(i).setDrawDeck(tempDrawDecks[i]);
        }
    }

    public static void runDraft(ArrayList<ClientData> clientData, ArrayList<Player> players, boolean isLastDraft) {
        notifyPlayersOfDraftStart(clientData, players);
        ArrayList<String> messages = new ArrayList<>();
        ExecutorService threadpool = Executors.newFixedThreadPool(clientData.size());

        executePlayerTasks(clientData, threadpool, messages);
        waitUntilAllTasksComplete(threadpool);
        processPlayerDrawMessages(messages, players);
        rotateDrawDeck(players, isLastDraft);
        System.out.println("Draft finished\n");
    }

    public static void runActivityPick(ArrayList<ClientData> clientData, ArrayList<Player> players, ArrayList<String> activities) {
        notifyPlayersOfActivityPick(clientData, players, activities);
        ArrayList<String> messages = new ArrayList<>();
        ExecutorService threadpool = Executors.newFixedThreadPool(clientData.size());

        executePlayerTasks(clientData, threadpool, messages);
        waitUntilAllTasksComplete(threadpool);
        processPlayerActivityMessages(messages, players);
        System.out.println("Activity picks finished\n");
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

    private static void processPlayerDrawMessages(ArrayList<String> messages, ArrayList<Player> players) {
        for (int i = 0; i < messages.size(); i++) {
            String message = messages.get(i);
            Player player = players.get(Character.getNumericValue(message.charAt(1)));
            DrawDeck drawDeck = player.getDrawDeck();
            DrawDeckCardMovement drawDeckCardMovement = new DrawDeckCardMovement();
            PlayerDeck moveToPlayerDeck = player.getPlayerDeck();
            String site = String.valueOf(message.charAt(0));
            player.setTouristSiteVisited(site);
            drawDeckCardMovement.moveCard(drawDeck, moveToPlayerDeck, site);
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
}
