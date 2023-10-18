package boomerang.scoring;

import boomerang.server.Player;

import java.util.ArrayList;

public class AustraliaScoringStrategy implements iScoringStrategy {
    private ArrayList<iScoringStrategy> strategies;

    public AustraliaScoringStrategy() {
        strategies = new ArrayList<>();

        // Add the boomerang.scoring strategies specific to this game mode
        //strategies.add(new ThrowAndCatchStrategy());
        //strategies.add(new CollectionStrategy());
        strategies.add(new AnimalStrategy());
        // Add more strategies if needed
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
