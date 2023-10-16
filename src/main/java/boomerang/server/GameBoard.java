package boomerang.server;

import java.util.ArrayList;


public class GameBoard {
    private final int TOTAL_ROUNDS = 4;
    private final int ROUND_LENGTH = 7;

    private int currentRound;
    private int currentDraft;


    ArrayList<ClientData> clientData;
    ArrayList<Player> players;

    public GameBoard(){
        this.clientData = new ArrayList<ClientData>();
        this.players = new ArrayList<Player>();

        this.currentRound = 1;
        this.currentDraft = 1;
    }
    public void addClient(ClientData clientData){
        this.clientData.add(clientData);
    }
    public void addPlayer(Player player){
        this.players.add(player);
    }

    public void runner(){
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


