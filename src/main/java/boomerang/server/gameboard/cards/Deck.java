package boomerang.server.gameboard.cards;

import java.util.ArrayList;

//base deck class
public class  Deck {
    protected ArrayList<Card> cards = new ArrayList<Card>();

    public ArrayList<Card> getCards(){
        return this.cards;
    }


}