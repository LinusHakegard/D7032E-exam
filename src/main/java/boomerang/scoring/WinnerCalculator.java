package boomerang.scoring;

import boomerang.server.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class WinnerCalculator {
    public static Player calculateWinner(ArrayList<Player> players) {
        // Sort players based on score and totalThrowCatchScore using a custom comparator.
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player player1, Player player2) {
                // Compare by score, and if scores are equal, use totalThrowCatchScore as a tiebreaker.
                if (player1.getScore() != player2.getScore()) {
                    return Integer.compare(player2.getScore(), player1.getScore()); // Descending order for score.
                } else {
                    return Integer.compare(player2.getPlayerThrowAndCatchScore(), player1.getPlayerThrowAndCatchScore()); // Descending order for totalThrowCatchScore.
                }
            }
        });

        // The player at index 0 of the sorted list is the winner.
        return players.get(0);
    }
}