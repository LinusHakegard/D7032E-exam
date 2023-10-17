package boomerang.cards;

import java.util.ArrayList;

public class GameBoardDeck extends Deck{

    public Card getFirstCard(){
        return super.cards.get(0);
    }
    public void removeFirstCard(){
        super.cards.remove(0);
    }

    public void setDeck(ArrayList<Card> cards){
        super.cards = cards;
    }
}