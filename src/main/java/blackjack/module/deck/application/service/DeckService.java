package blackjack.module.deck.application.service;

import blackjack.module.deck.application.usecase.CreateDeck;
import blackjack.module.deck.domain.entity.Deck;
import blackjack.module.deck.domain.service.DeckBuilder;
import org.springframework.stereotype.Service;


@Service
public class DeckService implements CreateDeck {

    @Override
    public Deck createDeck() {
        return DeckBuilder.createShuffledDeck();
    }
}
