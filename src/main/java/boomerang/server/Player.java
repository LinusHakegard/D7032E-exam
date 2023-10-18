package boomerang.server;

import boomerang.cards.PlayerDeck;

import java.util.ArrayList;

public class Player {
    int playerID;
    PlayerDeck playerDeck;

    ArrayList<String> usedActivities;
    public Player(int playerID){
        this.playerID = playerID;
        this.playerDeck = new PlayerDeck();
        this.usedActivities = new ArrayList<>();
    }
    public PlayerDeck getPlayerDeck(){return this.playerDeck;}
    public void resetPlayerDeck(){this.playerDeck.resetDeck();}

    public int getPlayerID(){return this.playerID;}
}
