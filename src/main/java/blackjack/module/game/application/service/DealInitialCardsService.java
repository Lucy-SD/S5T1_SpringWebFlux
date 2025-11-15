package blackjack.module.game.application.service;

import blackjack.module.game.application.usecase.DealInitialCards;
import blackjack.module.game.domain.entity.Game;
import blackjack.shared.exception.GameException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DealInitialCardsService implements DealInitialCards {

    @Override
    public Mono<Game> deal(Game game) {
        try {
            game.getPlayerHand().add(game.drawCardFromDeck());
            game.getDealerHand().add(game.drawCardFromDeck());
            game.getPlayerHand().add(game.drawCardFromDeck());
            game.getDealerHand().add(game.drawCardFromDeck());

            game.setPlayerScore(game.scoreCalculator(game.getPlayerHand()));
            game.setDealerScore(game.calculateVisibleScore());

            return Mono.just(game);
        } catch (Exception e) {
            return Mono.error(new GameException("Error al repartir las cartas iniciales."));
        }
    }
}
