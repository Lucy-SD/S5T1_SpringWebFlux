package blackjack.aahhrefact.module.deck.application.usecase;

import blackjack.aahhrefact.module.deck.domain.entity.Card;
import reactor.core.publisher.Mono;

public interface CardDrawer {
    void initializeDeck();
    Mono<Card> drawCard();
}
