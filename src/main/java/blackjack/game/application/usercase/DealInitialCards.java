package blackjack.game.application.usercase;

import blackjack.game.domain.Game;
import reactor.core.publisher.Mono;

public interface DealInitialCards {
    Mono<Game> execute (Game game);
}
