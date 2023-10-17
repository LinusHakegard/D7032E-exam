package boomerang.cards;

import java.util.ArrayList;
import java.util.Iterator;

public class DrawDeck extends Deck implements iAddableCardDeck{
    private int playerID;
    public DrawDeck(int playerID){
        this.playerID = playerID;
    }
    public ArrayList<Card> getCards(){return super.cards;}
    public void drawCard(Card card){super.cards.add(card);}

    public Card getCardByLetter(String cardLetter){
        for (Card card : cards) {
            if (card.getSite().equals(cardLetter)) {
                return card;
            }
        }
        return null;
    }

    public void removeCardByLetter(String cardLetter){
        Iterator<Card> iterator = cards.iterator();
        while (iterator.hasNext()) {
            Card card = iterator.next();
            if (card.getSite().equals(cardLetter)) {
                iterator.remove(); // Remove the card from the ArrayList
            }
        }
    }

    public void setPlayerID(int playerID){
        this.playerID = playerID;
    }
    public int getPlayerID(){return this.playerID;}

}
