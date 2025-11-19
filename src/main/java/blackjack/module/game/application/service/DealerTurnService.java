package blackjack.module.game.application.service;

import blackjack.module.deck.domain.valueObject.Card;
import blackjack.module.game.application.usecase.DealersTurn;
import blackjack.module.game.domain.entity.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DealerTurnService implements DealersTurn {

    @Override
    public Mono<Game> play(Game game) {
        game.setHasHiddenCard(false);
        game.setDealerScore(game.calculateVisibleScore());
        return drawUntil17(game);
    }

    private Mono<Game> drawUntil17(Game game) {
        if (game.getDealerScore() >= 17) {
            return Mono.just(game);
        }
        return Mono.fromCallable(() -> {
            Card card = game.drawCardFromDeck();
            game.getDealerHand().add(card);
            game.setDealerScore(game.calculateVisibleScore());
            return game;
        }).flatMap(this::drawUntil17);
    }
}
