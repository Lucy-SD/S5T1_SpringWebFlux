package blackjack.deck;

import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collections;

public class DeckService {

    private ArrayList<Card> deck;

    public DeckService() {
        deck = new ArrayList<>();
    }

    public Flux<Card> createDeck() {
        for (int value = 1; value <= 9; value++) {
            deck.addAll(Collections.nCopies(4, new Card(value)));
        }
        deck.addAll(Collections.nCopies(16, new Card(10)));


        return Flux.fromIterable(deck);
    }
}
