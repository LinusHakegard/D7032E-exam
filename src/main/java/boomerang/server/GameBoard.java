package boomerang.server;



import boomerang.australia.AustraliaActivities;
import boomerang.cards.DrawDeck;
import boomerang.cards.GameBoardDeck;
import boomerang.deckhandling.AustraliaCardLoaderJSON;
import boomerang.deckhandling.GameboardCardMovement;
import boomerang.scoring.AustraliaScoringStrategy;
import boomerang.scoring.MapScores;
import boomerang.scoring.WinnerCalculator;


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


    ArrayList<ClientData> clientData;

    ArrayList<DrawDeck> drawDecks;
    ArrayList<Player> players;

    GameBoardDeck gameBoardDeck;

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
            this.drawDecks.add(new DrawDeck(i));
            this.players.get(i).resetPlayerDeck();
        }

        GameboardCardMovement gameboardCardMovement = new GameboardCardMovement();
        for(DrawDeck drawDeck : this.drawDecks){
            for(int i=0; i < DRAW_DECK_START_SIZE; i++){
                gameboardCardMovement.moveCard(this.gameBoardDeck, drawDeck);
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

    public void announceWinner() {
        Player winner = WinnerCalculator.calculateWinner(this.players);

        String message = "The winner is: Player " + winner.getPlayerID();
        System.out.println(message);

        for (Player player : this.players) {
            MessageToClientSender.sendMessageToPlayer(clientData.get(player.getPlayerID()).getOutToClient(), message);
        }
    }
    public void runner(){
        initGame();


        while(this.currentRound <= this.TOTAL_ROUNDS) {
            System.out.println("new round");
            newRoundSetup();
            
            boolean finalRound = false;
            if(currentRound == this.TOTAL_ROUNDS){
                finalRound = true;
            }
            
            this.currentDraft = 1;
            RoundHandler.runDraft(this.clientData, this.players, this.drawDecks, true);
            this.currentDraft++;
            while(this.currentDraft <= this.ROUND_LENGTH){
                System.out.println("new draft");
                RoundHandler.runDraft(this.clientData, this.players, this.drawDecks, false);
                this.currentDraft++;
            }
            RoundHandler.runActivityPick(this.clientData, this.players, this.activities.getActivities());
            calculateScores(finalRound);
            this.currentRound++;
        }
        announceWinner();
    }
}


