package boomerang.server.gameboard.scoring;

import boomerang.server.gameboard.cards.PlayerDeck;
import boomerang.server.gameboard.Player;

import static java.lang.Math.abs;

//used for calculating scores based on throw and catch cards
public class ThrowAndCatchStrategy implements iScoringStrategy {
    @Override
    public int calculateScore(Player player) {
        int score = 0;

        PlayerDeck playerDeck = player.getPlayerDeck();
        score = abs(playerDeck.getLastCard().getNumber() - playerDeck.getFirstCard().getNumber());
        player.addToPlayerThrowAndCatchScore(score);
        return score;
    }
}