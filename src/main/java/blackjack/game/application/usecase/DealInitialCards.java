package blackjack.game.application.usecase;

import blackjack.game.domain.Game;
import reactor.core.publisher.Mono;

public interface DealInitialCards {
    Mono<Game> deal(Game game);
}
