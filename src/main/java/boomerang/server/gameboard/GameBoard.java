package boomerang.server.gameboard;

import boomerang.server.gameboard.australia.AustraliaActivities;
import boomerang.server.gameboard.cards.DrawDeck;
import boomerang.server.gameboard.cards.GameBoardDeck;
import boomerang.server.gameboard.australia.AustraliaCardLoaderJSON;
import boomerang.server.gameboard.deckhandling.GameboardCardMovement;
import boomerang.server.gameboard.australia.AustraliaScoringStrategy;
import boomerang.server.gameboard.scoring.MapScores;
import boomerang.server.gameboard.scoring.WinnerCalculator;

import java.util.ArrayList;


public class GameBoard {
    private final int TOTAL_ROUNDS = 4;
    private final int ROUND_LENGTH = 7;
    private final int DRAW_DECK_START_SIZE = 7;

    private int currentRound;
    private int currentDraft;

    private String country;

    private MapScores mapScores;

    private iCountryActivities activities;


    private final ArrayList<ClientData> clientData;

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

    //setting up game
    private void initGame() {
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

    //calculates scores
    private void calculateScores(boolean finalRound){
        mapScores.calculatePlayerMapBonuses(this.players);
        if(finalRound){
            mapScores.calculatePlayerVisitScore(this.players);
        }
        for(Player player : this.players){
            player.calculateScore();
        }
    }

    //tells MessageToClientSender to print the scores to the players
    public void announceScores(){
        for(Player player : players){
            String message = "Player " + player.getPlayerID() + " score: " + player.getScore();
            for (Player toPlayer : this.players) {
                MessageToClientSender.sendMessageToPlayer(clientData.get(toPlayer.getPlayerID()).getOutToClient(), message);
            }
        }
    }

    //tells MessageToClientSender to print the winner to the players
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

    //runs a single round
    private void runSingleRound() {
        newRoundSetup();
        boolean finalRound = (currentRound == this.TOTAL_ROUNDS);

        runDraftRounds();
        runActivityPickRound();
        calculateScores(finalRound);
        announceScores();
    }

    //runs the drafts
    private void runDraftRounds() {
        this.currentDraft = 1;

        while (this.currentDraft <= this.ROUND_LENGTH) {
            System.out.println("new draft");

            boolean lastRotation = false;
            if(this.currentDraft + 1 == this.ROUND_LENGTH){
                lastRotation = true;
            }

            RoundHandler.runDraft(this.clientData, this.players, lastRotation);
            this.currentDraft++;
        }
    }

    //runs the activity pick
    private void runActivityPickRound() {
        RoundHandler.runActivityPick(this.clientData, this.players, this.activities.getActivities());
    }
}


