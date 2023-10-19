package boomerang.server.scoring;

import boomerang.server.Player;

public interface iScoringStrategy {
    int calculateScore(Player player);
}
