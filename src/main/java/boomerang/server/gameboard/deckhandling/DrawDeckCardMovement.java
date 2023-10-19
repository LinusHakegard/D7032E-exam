package boomerang.server.gameboard.deckhandling;

import boomerang.server.gameboard.cards.DrawDeck;
import boomerang.server.gameboard.cards.iAddableCardDeck;

//moves card from DrawDeck to a deck class that implements iAddableCardDeck
public class DrawDeckCardMovement {
    public void moveCard (DrawDeck drawDeck, iAddableCardDeck moveToDeck, String cardLetter) {
        moveToDeck.drawCard(drawDeck.getCardByLetter(cardLetter));
        drawDeck.removeCardByLetter(cardLetter);
    }
}
