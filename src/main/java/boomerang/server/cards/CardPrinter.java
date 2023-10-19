package boomerang.server.cards;

import boomerang.server.Player;

import java.util.ArrayList;

public class CardPrinter {
    private static String formatCardInfo(Card card) {
        return "Name: " + card.getName()
                + " | Number: " + card.getNumber()
                + " | Site: " + card.getSite()
                + " | Region: " + card.getRegion()
                + " | Collection: " + card.getCollection()
                + " | Animal: " + card.getAnimal()
                + " | Activity: " + card.getActivity();
    }

    public static String getPlayerDeckContents(ArrayList<Player> players) {
        StringBuilder result = new StringBuilder();

        for (Player player : players) {
            result.append("Player " + player.getPlayerID() + "'s cards (excluding throw card):").append("\n");
            ArrayList<Card> playerCards = player.getPlayerDeck().getCards();

            for (int i = 1; i < playerCards.size(); i++) { // Start from index 1 to skip the first card
                Card card = playerCards.get(i);
                result.append(formatCardInfo(card)).append("\n");
            }

            result.append("\n");
        }

        return result.toString();
    }

    public static String getDrawDeckContents(DrawDeck drawDeck) {
        StringBuilder result = new StringBuilder();

        result.append("Available cards: ").append("\n");
        ArrayList<Card> cards = drawDeck.getCards();

        for (int i = 0; i < drawDeck.getCards().size(); i++) {
            Card card = cards.get(i);
            result.append(formatCardInfo(card)).append("\n");
        }

        result.append("\n");

        return result.toString();
    }

    public static String getPlayerThrowCardContents(Player player) {
        StringBuilder result = new StringBuilder();
        Card card = player.getPlayerDeck().getFirstCard();

        result.append("Your throw card is: ").append("\n");
        if (card != null) {
            result.append(formatCardInfo(card)).append("\n");
        }

        result.append("\n");

        return result.toString();
    }
}
