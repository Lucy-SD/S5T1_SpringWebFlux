package blackjack.game.application.usercase;

import blackjack.game.domain.Game;
import reactor.core.publisher.Mono;

public interface Hit {
    Mono<Game> hit(String gameId);
}
