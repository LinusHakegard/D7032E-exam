package boomerang.server.gameboard.scoring;

import boomerang.server.gameboard.Player;

//interface for scoring
public interface iScoringStrategy {
    int calculateScore(Player player);
}
