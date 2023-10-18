package boomerang.scoring;

import boomerang.server.Player;

public interface iScoringStrategy {
    int calculateScore(Player player);
}
