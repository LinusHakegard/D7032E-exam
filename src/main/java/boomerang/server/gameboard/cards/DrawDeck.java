package boomerang.server.gameboard.cards;

import java.util.ArrayList;
import java.util.Iterator;

//the decks that are passed around
public class DrawDeck extends Deck implements iAddableCardDeck {

    public ArrayList<Card> getCards() {
        return super.cards;
    }

    public void drawCard(Card card) {
        super.cards.add(card);
    }

    public Card getCardByLetter(String cardLetter) {
        for (Card card : super.cards) {
            if (card.getSite().equals(cardLetter)) {
                return card;
            }
        }
        return null;
    }

    public void removeCardByLetter(String cardLetter) {
        Iterator<Card> iterator = super.cards.iterator();
        while (iterator.hasNext()) {
            Card card = iterator.next();
            if (card.getSite().equals(cardLetter)) {
                iterator.remove(); // Remove the card from the ArrayList
            }
        }
    }
}
