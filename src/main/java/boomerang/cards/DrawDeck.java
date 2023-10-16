package boomerang.cards;

import java.util.ArrayList;

public class DrawDeck extends Deck implements iAddableCardDeck{

    public ArrayList<Card> getCards(){return super.cards;}
    public void drawCard(Card card){super.cards.add(card);}
    //remove att

}
