package boomerang.server.gameboard.deckhandling;

import boomerang.server.gameboard.cards.Card;
import java.util.ArrayList;

//can be implemented by different card readers depending on file type, game version
public interface iDeckLoader {
    ArrayList<Card> createCards();
}
