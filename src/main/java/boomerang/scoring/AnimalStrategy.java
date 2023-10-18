package boomerang.scoring;

import boomerang.cards.Card;
import boomerang.cards.PlayerDeck;
import boomerang.server.Player;

import java.util.ArrayList;
import java.util.Hashtable;

public class AnimalStrategy implements iScoringStrategy {
    Hashtable<String, Integer> animalToAmountMap;
    Hashtable<String, Integer> animalToScoreMap;

    public AnimalStrategy(){
        animalToAmountMap = new Hashtable<>();
        animalToAmountMap.put("Kangaroos", 0);
        animalToAmountMap.put("Koalas", 0);
        animalToAmountMap.put("Wombats", 0);
        animalToAmountMap.put("Emus", 0);
        animalToAmountMap.put("Platypuses", 0);

        animalToScoreMap = new Hashtable<>();
        animalToScoreMap.put("Kangaroos", 3);
        animalToScoreMap.put("Emus", 4);
        animalToScoreMap.put("Wombats", 5);
        animalToScoreMap.put("Koalas", 7);
        animalToScoreMap.put("Platypuses", 9);
    }

    @Override
    public int calculateScore(Player player) {
        int score = 0;

        PlayerDeck playerDeck = player.getPlayerDeck();
        for(Card card : playerDeck.getCards()){
            String animal = card.getAnimal();
            if(animal != null){
                int value = animalToAmountMap.get(animal);
                value++;
                animalToAmountMap.put(animal, value);
            }
        }

        for (String animal : animalToAmountMap.keySet()) {
            int count = animalToAmountMap.get(animal);
            int animalScore = animalToScoreMap.get(animal);

            if(count != 0){
                score += count/2 * animalScore;
            }

        }

        return score;
    }
}
