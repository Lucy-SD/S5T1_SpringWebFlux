package blackjack.module.deck.domain.service;

import blackjack.module.deck.domain.entity.Card;
import blackjack.module.deck.domain.entity.Deck;
import blackjack.shared.exception.GameException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckBuilder {

    private static List<Card> buildDeck() {
        List<Card> deck = new ArrayList<>();
        for (int value = 1; value <= 9; value++) {
            deck.addAll(Collections.nCopies(4, new Card(value)));
        }
        deck.addAll(Collections.nCopies(16, new Card(10)));
        return deck;
    }

    private static List<Card> shuffle(List<Card> deck) {
        List<Card> shuffledDeck = new ArrayList<>(deck);
        for (int i = 0; i <= 7; i++) {
            Collections.shuffle(shuffledDeck);
        }
        return shuffledDeck;
    }

    public static Deck createShuffledDeck() {
        List<Card> deck = buildDeck();
        List<Card> shuffledDeck = shuffle(deck);

        if(shuffledDeck.size() != 52) {
            throw new IllegalStateException("El mazo generado no tiene 52 cartas, sino "
            + shuffledDeck.size() + ".");
        }
        return new Deck(shuffledDeck);
    }
}
