package boomerang.server.gameboard.deckhandling;

import boomerang.server.gameboard.cards.*;

//moves card from GameBoardDeck to a deck class that implements iAddableCardDeck
public class GameboardCardMovement{
    public void moveCard (GameBoardDeck gameBoardDeck, iAddableCardDeck moveToDeck){
        moveToDeck.drawCard(gameBoardDeck.getFirstCard());
        gameBoardDeck.removeFirstCard();
    }


}
