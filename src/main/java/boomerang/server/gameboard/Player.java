package boomerang.server.gameboard;

import boomerang.server.gameboard.cards.DrawDeck;
import boomerang.server.gameboard.cards.PlayerDeck;
import boomerang.server.gameboard.scoring.iScoringStrategy;

import java.util.ArrayList;

public class Player {
    private int playerID;
    private int score;
    private int totalThrowCatchScore;
    private String mostRecentActivity;
    private PlayerDeck playerDeck;
    private DrawDeck drawDeck;
    private iScoringStrategy countryScoringStrategy;
    private ArrayList<String> usedActivities;
    private ArrayList<String> touristSitesVisited;

    public Player(int playerID) {
        this.playerID = playerID;
        this.score = 0;
        this.totalThrowCatchScore = 0;
        this.playerDeck = new PlayerDeck();
        this.usedActivities = new ArrayList<>();
        this.touristSitesVisited = new ArrayList<>();
    }

    // Accessor methods
    public int getPlayerID() {
        return playerID;
    }

    public int getScore() {
        return score;
    }

    public int getPlayerThrowAndCatchScore() {
        return totalThrowCatchScore;
    }

    public PlayerDeck getPlayerDeck() {
        return playerDeck;
    }

    public DrawDeck getDrawDeck() {
        return drawDeck;
    }

    public String getMostRecentActivity() {
        return mostRecentActivity;
    }

    public ArrayList<String> getUsedActivities() {
        return usedActivities;
    }

    public ArrayList<String> getTouristSitesVisited() {
        return touristSitesVisited;
    }

    // Mutator methods
    public void setDrawDeck(DrawDeck drawDeck) {
        this.drawDeck = drawDeck;
    }

    public void setCountryScoringStrategy(iScoringStrategy countryScoringStrategy) {
        this.countryScoringStrategy = countryScoringStrategy;
    }

    public void setMostRecentActivity(String activity) {
        this.mostRecentActivity = activity;
    }

    public void addToPlayerScore(int score) {
        this.score += score;
    }

    public void addToPlayerThrowAndCatchScore(int score) {
        this.totalThrowCatchScore += score;
    }

    public void setTouristSiteVisited(String site) {
        if (!touristSitesVisited.contains(site)) {
            touristSitesVisited.add(site);
        }
    }

    public void calculateScore() {
        if (countryScoringStrategy != null) {
            this.score += countryScoringStrategy.calculateScore(this);
            System.out.println("Player score = " + this.score);
        }
    }

    public void resetPlayerDeck() {
        playerDeck.resetDeck();
    }

    public void addUsedActivity(String activity) {
        usedActivities.add(activity);
    }
}
