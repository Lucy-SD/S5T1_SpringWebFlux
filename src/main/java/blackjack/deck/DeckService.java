package blackjack.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckService {

    private List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();
        for (int value = 1; value <= 9; value++) {
            deck.addAll(Collections.nCopies(4, new Card(value)));
        }
        deck.addAll(Collections.nCopies(16, new Card(10)));
        return deck;
    }

    private List<Card> shuffleDeck(List<Card> deck) {
        for (int i = 0; i <= 7; i++) {
            Collections.shuffle(deck);
        }
        return deck;
    }

}
