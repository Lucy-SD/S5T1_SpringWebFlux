package blackjack.aahhrefact.module.game.application.service;

import blackjack.aahhrefact.module.deck.application.usecase.CardDrawer;
import blackjack.aahhrefact.module.game.application.usecase.DealersTurn;
import blackjack.aahhrefact.module.game.domain.entity.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DealerTurnService implements DealersTurn {

    private final CardDrawer cardDrawer;

    @Override
    public Mono<Game> play(Game game) {
        game.setFirstCardHidden(false);
        game.setDealerScore(game.calculateVisibleScore());
        return drawUntil17(game);
    }

    private Mono<Game> drawUntil17(Game game) {
        if (game.getDealerScore() >= 17) {
            return Mono.just(game);
        }

        return cardDrawer.drawCard()
                .doOnNext(card -> game.getDealerHand().add(card))
                .doOnNext(card -> game.setDealerScore(game.calculateVisibleScore()))
                .thenReturn(game)
                .flatMap(this::drawUntil17);
    }
}
