package boomerang.cards;

import boomerang.cards.Card;

import java.util.ArrayList;
//göra abstakt och lägga till metoder
public class Deck {
    protected ArrayList<Card> cards = new ArrayList<Card>();

    public ArrayList<Card> getCards(){
        return this.cards;
    }


}