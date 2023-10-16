package boomerang.server;

import java.util.ArrayList;

class Deck {
    protected ArrayList<Card> cards = new ArrayList<Card>();

    public void setDeck(ArrayList<Card> cards){
        this.cards = cards;
    }
    public Card getFirstCard(){
        return cards.get(0);
    }
    public void removeFirstCard(){
        this.cards.remove(0);
    }
}