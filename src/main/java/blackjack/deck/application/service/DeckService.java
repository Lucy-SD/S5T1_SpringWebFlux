package blackjack.deck.application.service;

import blackjack.deck.domain.Card;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DeckService implements CardDrawer {
    private Flux<Card> currentDeck;
    private AtomicInteger usedCards = new AtomicInteger(0);

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

    private void initializeDeck() {
        List<Card> deck = createDeck();
        List<Card> shuffled = shuffleDeck(deck);
        this.currentDeck = Flux.fromIterable(shuffled).cache();
        this.usedCards.set(0);
    }

    @Override
    public Mono<Card> drawCard() {
        if (this.currentDeck == null) {
            initializeDeck();
        }
        int nextCardIndex = this.usedCards.getAndIncrement();
        return currentDeck
                .elementAt(nextCardIndex)
                .onErrorMap(throwable ->
                        new IllegalStateException("No hay más cartas en el mazo.", throwable));
    }
}
