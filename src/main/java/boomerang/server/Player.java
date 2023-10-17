package boomerang.server;

import boomerang.cards.PlayerDeck;

public class Player {
    int playerID;
    PlayerDeck playerDeck;
    public Player(int playerID){
        this.playerID = playerID;
        this.playerDeck = new PlayerDeck();
    }
    public PlayerDeck getPlayerDeck(){return this.playerDeck;}
}
