package boomerang.server;

import boomerang.cards.Deck;
import boomerang.cards.PlayerDeck;
import boomerang.scoring.iScoringStrategy;

import java.util.ArrayList;

public class Player {
    int playerID;
    PlayerDeck playerDeck;

    int score;
    int totalThrowCatchScore;
    iScoringStrategy countryScoringStrategy;

    ArrayList<String> usedActivities;
    ArrayList<String> touristSitesVisited;
    String mostRecentActivity;
    public Player(int playerID){
        this.playerID = playerID;
        this.playerDeck = new PlayerDeck();
        this.usedActivities = new ArrayList<>();
        this.touristSitesVisited = new ArrayList<>();

        this.score = 0;
        this.totalThrowCatchScore = 0;

    }
    public PlayerDeck getPlayerDeck(){return this.playerDeck;}
    public void resetPlayerDeck(){this.playerDeck.resetDeck();}

    public int getPlayerID(){return this.playerID;}

    public ArrayList<String> getUsedActivities(){
        return this.usedActivities;
    }

    public void addUsedActivity(String activity){
        this.usedActivities.add(activity);
    }
    public void setMostRecentActivity(String activity){
        this.mostRecentActivity = activity;
    }

    public void setTouristSiteVisited(String site){
        if(!touristSitesVisited.contains(site)){
            this.touristSitesVisited.add(site);
        }

    }

    public void setCountryScoringStrategy(iScoringStrategy countryScoringStrategy){
        this.countryScoringStrategy = countryScoringStrategy;
    }
    public void calculateScore(){
        this.score += this.countryScoringStrategy.calculateScore(this);
        System.out.println("player score = " + this.score);
    }

    public String getMostRecentActivity(){
        return this.mostRecentActivity;
    }

    public ArrayList<String> getTouristSitesVisited(){
        return this.touristSitesVisited;
    }

    public void addToPlayerScore(int score){
        this.score += score;
    }
    public void addToPlayerThrowAndCatchScore(int score){
        this.totalThrowCatchScore += score;
    }

    public int getScore(){
        return this.score;
    }
    public int getPlayerThrowAndCatchScore(){
        return this.totalThrowCatchScore;
    }
}
