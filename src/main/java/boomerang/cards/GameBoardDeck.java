package boomerang.cards;

import java.util.ArrayList;
import java.util.Collections;

public class GameBoardDeck extends Deck{

    public Card getFirstCard(){
        return super.cards.get(0);
    }
    public void removeFirstCard(){
        super.cards.remove(0);
    }

    public void setDeck(ArrayList<Card> cards){
        super.cards.clear();
        super.cards = cards;
    }

    public void shuffleDeck(){
        Collections.shuffle(super.cards);
    }
}
