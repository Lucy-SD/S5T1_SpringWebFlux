package blackjack.aahhrefact.module.deck.application.service;

import blackjack.aahhrefact.module.deck.application.usecase.CreateDeck;
import blackjack.aahhrefact.module.deck.domain.entity.Deck;
import blackjack.aahhrefact.module.deck.domain.service.DeckBuilder;
import org.springframework.stereotype.Service;


@Service
public class DeckService implements CreateDeck {

    @Override
    public Deck createDeck() {
        return DeckBuilder.createShuffledDeck();
    }
}
