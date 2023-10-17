package boomerang.deckhandling;

import boomerang.cards.DrawDeck;
import boomerang.cards.GameBoardDeck;
import boomerang.cards.iAddableCardDeck;

public class DrawDeckCardMovement {
    public void moveCard (DrawDeck drawDeck, iAddableCardDeck moveToDeck, String cardLetter) {
        moveToDeck.drawCard(drawDeck.getCardByLetter(cardLetter));
        drawDeck.removeCardByLetter(cardLetter);
    }
}
