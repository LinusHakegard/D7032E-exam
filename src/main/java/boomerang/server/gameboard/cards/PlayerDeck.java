package boomerang.server.gameboard.cards;

import java.util.ArrayList;

//deck class for the cards that the player has chosen
public class PlayerDeck extends Deck implements iAddableCardDeck {
    public ArrayList<Card> getCards() {
        return super.cards;
    }

    public void drawCard(Card card) {
        super.cards.add(card);
    }

    public void resetDeck() {
        super.cards.clear();
    }

    public Card getFirstCard() {
        if(super.cards.size() > 0) {
            return super.cards.get(0);
        }
        return null;
    }
    public Card getLastCard() {
        if(super.cards.size() > 0) {
            return super.cards.get(super.cards.size() - 1);
        }
        return null;
    }

}