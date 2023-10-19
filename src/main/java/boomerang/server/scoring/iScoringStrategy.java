package boomerang.server.scoring;

import boomerang.server.gameboard.Player;

public interface iScoringStrategy {
    int calculateScore(Player player);
}
