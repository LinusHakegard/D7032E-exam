package boomerang.server.gameboard.australia;

import boomerang.server.gameboard.Player;
import boomerang.server.gameboard.scoring.*;

import java.util.ArrayList;

public class AustraliaScoringStrategy implements iScoringStrategy {
    private final ArrayList<iScoringStrategy> strategies;

    //adding the desired scoring for the australia game version
    public AustraliaScoringStrategy() {
        strategies = new ArrayList<>();

        strategies.add(new ThrowAndCatchStrategy());
        strategies.add(new CollectionStrategy());
        strategies.add(new AnimalStrategy());
        strategies.add(new ActivityStrategy());
    }

    @Override
    public int calculateScore(Player player) {
        int totalScore = 0;
        for (iScoringStrategy strategy : strategies) {
            totalScore += strategy.calculateScore(player);
        }
        return totalScore;
    }
}
