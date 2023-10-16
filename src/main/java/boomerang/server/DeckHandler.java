package boomerang.server;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DeckHandler extends Deck {

    public DeckHandler() {

    }
    public ArrayList<Card> createAustraliaCardsFromJSONFile() {
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

    public ArrayList<Card> getPicks(Deck deck, int amount){
        ArrayList<Card> picks = new ArrayList<Card>();
        for(int i=0; i<amount; i++){
            picks.add(deck.getFirstCard());
            deck.removeFirstCard();
        }
        return picks;
    }
    public static void main(String[] args) {
        Deck deck = new Deck();
        DeckHandler deckHandler = new DeckHandler();
        deck.setDeck(deckHandler.createAustraliaCardsFromJSONFile());
        //ArrayList<Card> testHand = deckHandler.getPicks(deck,1);

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
