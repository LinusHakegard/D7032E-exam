package boomerang.deckhandling;

import boomerang.cards.*;

public class GameboardCardMovement implements iCardMovement{
    public void moveCard (GameBoardDeck gameBoardDeck, iAddableCardDeck drawDeck){
        drawDeck.drawCard(gameBoardDeck.getFirstCard());
        gameBoardDeck.removeFirstCard();
    }
}
