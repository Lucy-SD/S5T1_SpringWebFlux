package blackjack.aahhrefact.module.game.application.service;

import blackjack.aahhrefact.module.deck.application.usecase.CardDrawer;
import blackjack.aahhrefact.module.game.application.usecase.DealInitialCards;
import blackjack.aahhrefact.module.game.domain.entity.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DealInitialCardsService implements DealInitialCards {

    private final CardDrawer cardDrawer;

    @Override
    public Mono<Game> deal(Game game) {
        cardDrawer.initializeDeck(game.getPlayerId().toString());
        return cardDrawer.drawCard()
                .doOnNext(card -> game.getPlayerHand().add(card))
                .then(cardDrawer.drawCard())
                .doOnNext(card -> game.getDealerHand().add(card))
                .then(cardDrawer.drawCard())
                .doOnNext(card -> game.getPlayerHand().add(card))
                .then(cardDrawer.drawCard())
                .doOnNext(card -> game.getDealerHand().add(card))
                .doOnNext(card -> {
                    game.setPlayerScore(game.scoreCalculator(game.getPlayerHand()));
                    game.setDealerScore(game.calculateVisibleScore());
                })
                .thenReturn(game);
    }
}
