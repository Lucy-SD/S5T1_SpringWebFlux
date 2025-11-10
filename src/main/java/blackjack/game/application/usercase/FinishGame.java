package blackjack.game.application.usercase;

import blackjack.game.domain.Game;
import reactor.core.publisher.Mono;

public interface FinishGame {
    Mono<Game> execute (String gameId);
}
