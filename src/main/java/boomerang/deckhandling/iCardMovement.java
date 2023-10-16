package boomerang.deckhandling;

import boomerang.cards.GameBoardDeck;
import boomerang.cards.DrawDeck;
import boomerang.cards.iAddableCardDeck;

public interface iCardMovement {
    public void moveCard(GameBoardDeck gameBoardDeck, iAddableCardDeck drawDeck);
}
