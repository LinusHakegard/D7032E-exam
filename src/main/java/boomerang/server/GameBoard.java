package boomerang.server;



import boomerang.cards.GameBoardDeck;
import boomerang.deckhandling.AustraliaCardLoaderJSON;

import java.util.ArrayList;


public class GameBoard {
    private final int TOTAL_ROUNDS = 4;
    private final int ROUND_LENGTH = 7;

    private int currentRound;
    private int currentDraft;


    ArrayList<ClientData> clientData;
    ArrayList<Player> players;

    GameBoardDeck gameBoardDeck;

    public GameBoard(){
        this.clientData = new ArrayList<ClientData>();
        this.players = new ArrayList<Player>();

        this.gameBoardDeck = new GameBoardDeck();

        this.currentRound = 1;
        this.currentDraft = 1;
    }
    public void addClient(ClientData clientData){
        this.clientData.add(clientData);
    }
    public void addPlayer(Player player){
        this.players.add(player);
    }

    private void initGame(){
        //setting up decks
        AustraliaCardLoaderJSON cardLoader = new AustraliaCardLoaderJSON();
        gameBoardDeck.setDeck(cardLoader.createCards());

    }

    public void runner(){
        initGame();
        RoundHandler roundHandler = new RoundHandler(clientData);
        while(this.TOTAL_ROUNDS <= 4) {
            roundHandler.notifyPlayersOfRoundStart();
            roundHandler.startRound();
            while(this.ROUND_LENGTH <= 6){
                roundHandler.startRound();
            }
        }
    }
}


