package boomerang.scoring;

import boomerang.server.Player;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Set;

public class MapScores {
    private Hashtable<String, Integer> touristSiteBonuses = new Hashtable<>();
    private Set<String> regionsToRemove = new HashSet<>();

    public MapScores() {
        // Initialize the touristSiteBonuses map with regions and their respective bonuses.
        touristSiteBonuses.put("ABCD", 3);
        touristSiteBonuses.put("EFGH", 3);
        touristSiteBonuses.put("IJKL", 3);
        touristSiteBonuses.put("MNOP", 3);
        touristSiteBonuses.put("QRST", 3);
        touristSiteBonuses.put("UVWX", 3);
        touristSiteBonuses.put("YZ*-", 3);
        // Add more regions and bonuses as needed.
    }

    public void calculatePlayerMapBonuses(ArrayList<Player> players) {
        for (Player player : players) {
            ArrayList<String> playerVisitedLocations = player.getTouristSitesVisited();
            int totalBonus = 0;

            for (String region : touristSiteBonuses.keySet()) {
                // Check if the player has visited all the letters of the bonus region.
                boolean hasAllLetters = true;
                for (char letter : region.toCharArray()) {
                    if (!playerVisitedLocations.contains(String.valueOf(letter))) {
                        hasAllLetters = false;
                        break;
                    }
                }

                if (hasAllLetters) {
                    int bonus = touristSiteBonuses.get(region);
                    totalBonus += bonus;
                    // Add the region to the list of regions to remove.
                    regionsToRemove.add(region);
                }
            }

            // Update the player's score with the total bonus.
            player.addToPlayerScore(totalBonus);
        }

        // Remove the regions that have been checked by all players.
        for (String regionToRemove : regionsToRemove) {
            touristSiteBonuses.remove(regionToRemove);
        }
    }

    public void calculatePlayerVisitScore(ArrayList<Player> players) {

        for(Player player : players){
            int score = player.getTouristSitesVisited().size();
            player.addToPlayerScore(score);
        }
    }
}
