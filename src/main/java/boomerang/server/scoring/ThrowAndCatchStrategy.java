package boomerang.server.scoring;

import boomerang.server.cards.PlayerDeck;
import boomerang.server.gameboard.Player;

import static java.lang.Math.abs;

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