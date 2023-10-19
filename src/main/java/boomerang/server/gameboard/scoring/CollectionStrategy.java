package boomerang.server.gameboard.scoring;

import boomerang.server.gameboard.cards.Card;
import boomerang.server.gameboard.cards.PlayerDeck;
import boomerang.server.gameboard.Player;
import java.util.Hashtable;

//used for calculating scores based on collections
public class CollectionStrategy implements iScoringStrategy {
    Hashtable<String, Integer> wordToIntegerMap;

    public CollectionStrategy(){
        wordToIntegerMap = new Hashtable<>();
        wordToIntegerMap.put("Leaves", 1);
        wordToIntegerMap.put("Wildflowers", 2);
        wordToIntegerMap.put("Shells", 3);
        wordToIntegerMap.put("Souvenirs", 5);
    }

    @Override
    public int calculateScore(Player player) {
        int score = 0;

        PlayerDeck playerDeck = player.getPlayerDeck();
        for(Card card : playerDeck.getCards()){
            String collection = card.getCollection();
            if(collection != null){
                score += wordToIntegerMap.get(collection);
            }

        }

        if(score <= 7){
            score *= 2;
        }
        return score;
    }
}
