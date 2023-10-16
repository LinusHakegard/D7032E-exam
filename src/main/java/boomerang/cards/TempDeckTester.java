package boomerang.cards;

import boomerang.deckhandling.AustraliaCardLoaderJSON;
import boomerang.deckhandling.GameboardCardMovement;

public class TempDeckTester {
    public static void main(String[] args) {
        GameBoardDeck gameBoardDeck = new GameBoardDeck();
        DrawDeck drawDeck = new DrawDeck();
        AustraliaCardLoaderJSON cardLoader = new AustraliaCardLoaderJSON();

        gameBoardDeck.setDeck(cardLoader.createCards());
        GameboardCardMovement mover = new GameboardCardMovement();
        //mover.moveCard(gameBoardDeck, playerDeck);
        //mover.moveCard(gameBoardDeck, playerDeck);

        for (Card card : gameBoardDeck.getCards()) {
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




