package boomerang.server;



import boomerang.cards.DrawDeck;
import boomerang.cards.GameBoardDeck;
import boomerang.deckhandling.AustraliaCardLoaderJSON;
import boomerang.deckhandling.GameboardCardMovement;

import java.util.ArrayList;


public class GameBoard {
    private final int TOTAL_ROUNDS = 3;
    private final int ROUND_LENGTH = 2;
    private final int DRAW_DECK_START_SIZE = 3;

    private int currentRound;
    private int currentDraft;




    ArrayList<ClientData> clientData;

    ArrayList<DrawDeck> drawDecks;
    ArrayList<Player> players;

    GameBoardDeck gameBoardDeck;

    public GameBoard(){
        this.clientData = new ArrayList<ClientData>();
        this.players = new ArrayList<Player>();


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



    }
    private void newRoundSetup(){

        //setting up decks
        AustraliaCardLoaderJSON cardLoader = new AustraliaCardLoaderJSON();
        this.gameBoardDeck.setDeck(cardLoader.createCards());
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

       /* for (Card card : this.gameBoardDeck.getCards()) {
            System.out.println("Name: " + card.getName());
            System.out.println("Number: " + card.getNumber());
            System.out.println("Site: " + card.getSite());
            System.out.println("Region: " + card.getRegion());
            System.out.println("Collection: " + card.getCollection());
            System.out.println("Animal: " + card.getAnimal());
            System.out.println("Activity: " + card.getActivity());
            System.out.println();
        }
        System.out.println("player0 cards" );
        System.out.println("" );
        for (Card card : drawDecks.get(0).getCards()) {
            System.out.println("Name: " + card.getName());
            System.out.println("Number: " + card.getNumber());
            System.out.println("Site: " + card.getSite());
            System.out.println("Region: " + card.getRegion());
            System.out.println("Collection: " + card.getCollection());
            System.out.println("Animal: " + card.getAnimal());
            System.out.println("Activity: " + card.getActivity());
            System.out.println();
        }*/
    }

    public void runner(){
        initGame();

        RoundHandler roundHandler = new RoundHandler(clientData);
        while(this.currentRound <= this.TOTAL_ROUNDS) {
            System.out.println("new round");
            newRoundSetup();
            this.currentDraft = 1;
            while(this.currentDraft <= this.ROUND_LENGTH){
                System.out.println("new draft");
                roundHandler.run(this.players, this.drawDecks);
                this.currentDraft++;

            }
            this.currentRound++;
        }
    }
}


