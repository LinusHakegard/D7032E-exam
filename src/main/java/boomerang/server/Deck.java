package boomerang.server;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Deck {
    private ArrayList<Card> cards;
    public Deck() {
        cards = new ArrayList<>();
        this.createCardsFromJSONFile("src/main/resources/AustraliaCards.json");
    }
    public void createCardsFromJSONFile(String jsonFilePath) {
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

                this.cards.add(card);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Card> getCards(){
        return this.cards;
    }
    public static void main(String[] args) {
        Deck deck = new Deck();
        for (Card card : deck.cards) {
            System.out.println("Name: " + card.getName());
            System.out.println("Number: " + card.getNumber());
            System.out.println("Site: " + card.getSite());
            System.out.println("Region: " + card.getRegion());
            System.out.println("Collection: " + card.getCollection());
            System.out.println("Animal: " + card.getAnimal());
            System.out.println("Activity: " + card.getActivity());
            System.out.println();
        }
    }
}
