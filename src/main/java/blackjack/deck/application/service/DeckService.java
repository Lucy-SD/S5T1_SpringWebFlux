package blackjack.deck.application.service;

import blackjack.aahhrefact.module.deck.application.usecase.CardDrawer;
import blackjack.aahhrefact.module.deck.domain.entity.Card;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DeckService implements CardDrawer {
    private Flux<Card> currentDeck;
    private AtomicInteger usedCards = new AtomicInteger(0);



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
