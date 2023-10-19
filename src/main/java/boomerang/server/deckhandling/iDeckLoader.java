package boomerang.server.deckhandling;

import boomerang.server.cards.Card;
import java.util.ArrayList;

public interface iDeckLoader {
    public ArrayList<Card> createCards();
}
