package boomerang.server.scoring;

import boomerang.server.cards.Card;
import boomerang.server.Player;

import java.util.Hashtable;

import static java.lang.Math.abs;

public class ActivityStrategy implements iScoringStrategy{
    Hashtable<Integer, Integer> amountToScoreMap;
    public ActivityStrategy(){
        amountToScoreMap = new Hashtable<>();
        amountToScoreMap.put(0, 0);
        amountToScoreMap.put(1, 0);
        amountToScoreMap.put(2, 2);
        amountToScoreMap.put(3, 4);
        amountToScoreMap.put(4, 7);
        amountToScoreMap.put(5, 10);
        amountToScoreMap.put(6, 15);
    }
    @Override
    public int calculateScore(Player player) {
        int score = 0;
        String activityChoice = player.getMostRecentActivity();

        int amount = 0;
        for(Card card :  player.getPlayerDeck().getCards()){
            if(card.getActivity() != null){
                if(card.getActivity().equals(activityChoice)){
                    amount += 1;
                }
            }
        }

        score = amountToScoreMap.get(amount);


        return score;
    }
}