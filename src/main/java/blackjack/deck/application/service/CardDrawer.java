package blackjack.deck.application.service;

import blackjack.deck.domain.Card;
import reactor.core.publisher.Mono;

public interface CardDrawer {
    Mono<Card> drawCard();
}
