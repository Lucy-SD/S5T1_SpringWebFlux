package blackjack.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckService {

    private ArrayList<Card> deck;

    public DeckService() {
        deck = new ArrayList<>();
    }

    public List<Card> createDeck() {
        for (int value = 1; value <= 9; value++) {
            deck.addAll(Collections.nCopies(4, new Card(value)));
        }
        deck.addAll(Collections.nCopies(16, new Card(10)));
        return deck;
    }
}
