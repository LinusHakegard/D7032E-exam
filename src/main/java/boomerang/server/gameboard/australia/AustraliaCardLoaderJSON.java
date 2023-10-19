package boomerang.server.gameboard.australia;

import boomerang.server.gameboard.cards.Card;
import boomerang.server.gameboard.deckhandling.iDeckLoader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class AustraliaCardLoaderJSON implements iDeckLoader {
    //returns arraylist of the cards specified in the json file
    public ArrayList<Card> createCards(){
        ArrayList<Card> cards = new ArrayList<>();
        String jsonFilePath = "src/main/resources/AustraliaCards.json";
            try {
            JSONParser parser = new JSONParser();
            JSONArray jsonData = (JSONArray) parser.parse(new FileReader(jsonFilePath));

            for (Object item : jsonData) {
                JSONObject cardData = (JSONObject) item;

                String name = (String) cardData.get("name");
                long number = (Long) cardData.get("number");
                String site = (String) cardData.get("site");
                String region = (String) cardData.get("region");
                String collection = (String) cardData.get("collection");
                String animal = (String) cardData.get("animal");
                String activity = (String) cardData.get("activity");

                Card card = new Card.Builder()
                        .name(name)
                        .number((int) number)
                        .site(site)
                        .region(region)
                        .collection(collection)
                        .animal(animal)
                        .activity(activity)
                        .build();

                cards.add(card);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
            return cards;
    }
}
