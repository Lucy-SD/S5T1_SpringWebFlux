package blackjack.aahhrefact.module.deck.application.service;

import blackjack.aahhrefact.module.deck.application.usecase.CardDrawer;
import blackjack.aahhrefact.module.deck.domain.entity.Card;
import blackjack.aahhrefact.module.deck.domain.service.DeckBuilder;
import blackjack.exception.GameException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeckService implements CardDrawer {

    private List<Card> currentDeck = new ArrayList<>();
    private int deckPointer = 0;

    @Override
    public void initializeDeck(String playerId) {
        this.currentDeck = DeckBuilder.createShuffledDeck();
        this.deckPointer = 0;
    }

    @Override
    public Mono<Card> drawCard() {
        return Mono.fromCallable(() -> {
            if (deckPointer >= currentDeck.size()) {
                throw new GameException("No quedan cartas en el mazo.");
            }
            Card card = currentDeck.get(deckPointer);
            deckPointer++;
            return card;
        });
    }
}
