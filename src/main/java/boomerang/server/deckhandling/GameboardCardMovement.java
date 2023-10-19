package boomerang.server.deckhandling;

import boomerang.server.cards.*;

public class GameboardCardMovement{
    public void moveCard (GameBoardDeck gameBoardDeck, iAddableCardDeck moveToDeck){
        moveToDeck.drawCard(gameBoardDeck.getFirstCard());
        gameBoardDeck.removeFirstCard();
    }


}
