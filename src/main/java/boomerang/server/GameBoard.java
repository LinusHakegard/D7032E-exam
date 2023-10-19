package boomerang.server;

import boomerang.server.australia.AustraliaActivities;
import boomerang.server.cards.DrawDeck;
import boomerang.server.cards.GameBoardDeck;
import boomerang.server.australia.AustraliaCardLoaderJSON;
import boomerang.server.deckhandling.GameboardCardMovement;
import boomerang.server.australia.AustraliaScoringStrategy;
import boomerang.server.scoring.MapScores;
import boomerang.server.scoring.WinnerCalculator;


import java.util.ArrayList;


public class GameBoard {
    private final int TOTAL_ROUNDS = 2;
    private final int ROUND_LENGTH = 2;
    private final int DRAW_DECK_START_SIZE = 7;

    private int currentRound;
    private int currentDraft;

    private String country;

    private MapScores mapScores;

    private iCountryActivities activities;


    private ArrayList<ClientData> clientData;

    private ArrayList<DrawDeck> drawDecks;
    private ArrayList<Player> players;

    private GameBoardDeck gameBoardDeck;

    public GameBoard(String country){
        this.clientData = new ArrayList<ClientData>();
        this.players = new ArrayList<Player>();
        this.country = country;



    }
    public void addClient(ClientData clientData){
        this.clientData.add(clientData);
    }
    public void addPlayer(Player player){
        this.players.add(player);
    }


    private void initGame() {
        //setting up states
        this.currentRound = 1;
        this.currentDraft = 1;
        this.gameBoardDeck = new GameBoardDeck();
        this.drawDecks = new ArrayList<DrawDeck>();
        this.mapScores = new MapScores();

        if(country.equals("Australia")) {
            this.activities = new AustraliaActivities();

            for (Player player : players) {
                player.setCountryScoringStrategy(new AustraliaScoringStrategy());
            }
        }

    }
    private void newRoundSetup(){

        //setting up decks
        AustraliaCardLoaderJSON cardLoader = new AustraliaCardLoaderJSON();
        this.gameBoardDeck.setDeck(cardLoader.createCards());
        this.gameBoardDeck.shuffleDeck();
        this.drawDecks.clear();

        for(int i=0; i<players.size(); i++){
            this.players.get(i).setDrawDeck(new DrawDeck());
            this.players.get(i).resetPlayerDeck();
        }

        GameboardCardMovement gameboardCardMovement = new GameboardCardMovement();
        for(Player player : players){
            for(int i=0; i < DRAW_DECK_START_SIZE; i++){
                gameboardCardMovement.moveCard(this.gameBoardDeck, player.getDrawDeck());
            }
        }


    }
    private void calculateScores(boolean finalRound){
        mapScores.calculatePlayerMapBonuses(this.players);
        if(finalRound){
            mapScores.calculatePlayerVisitScore(this.players);
        }
        for(Player player : this.players){
            player.calculateScore();
        }

    }
    public void announceScores(){
        for(Player player : players){
            String message = "Player " + player.getPlayerID() + " score: " + player.getScore();
            for (Player toPlayer : this.players) {
                MessageToClientSender.sendMessageToPlayer(clientData.get(toPlayer.getPlayerID()).getOutToClient(), message);
            }
        }
    }

    public void announceWinner() {
        Player winner = WinnerCalculator.calculateWinner(this.players);

        String message = "The winner is: Player " + winner.getPlayerID();
        System.out.println(message);

        for (Player player : this.players) {
            MessageToClientSender.sendMessageToPlayer(clientData.get(player.getPlayerID()).getOutToClient(), message);
        }
    }

    //This keeps the game running
    public void runner() {
        initGame();

        while (this.currentRound <= this.TOTAL_ROUNDS) {
            System.out.println("new round");
            runSingleRound();
            this.currentRound++;
        }

        announceWinner();
    }

    private void runSingleRound() {
        newRoundSetup();
        boolean finalRound = (currentRound == this.TOTAL_ROUNDS);

        runDraftRounds(finalRound);
        runActivityPickRound();
        calculateScores(finalRound);
        announceScores();
    }

    private void runDraftRounds(boolean finalRound) {
        this.currentDraft = 1;

        while (this.currentDraft <= this.ROUND_LENGTH) {
            System.out.println("new draft");
            RoundHandler.runDraft(this.clientData, this.players);
            this.currentDraft++;
        }
    }

    private void runActivityPickRound() {
        RoundHandler.runActivityPick(this.clientData, this.players, this.activities.getActivities());
    }
}


