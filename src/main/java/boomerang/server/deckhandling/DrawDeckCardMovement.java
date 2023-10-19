package boomerang.server.deckhandling;

import boomerang.server.cards.DrawDeck;
import boomerang.server.cards.iAddableCardDeck;

public class DrawDeckCardMovement {
    public void moveCard (DrawDeck drawDeck, iAddableCardDeck moveToDeck, String cardLetter) {
        moveToDeck.drawCard(drawDeck.getCardByLetter(cardLetter));
        drawDeck.removeCardByLetter(cardLetter);
    }
}
