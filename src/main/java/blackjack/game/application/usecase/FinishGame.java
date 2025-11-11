package blackjack.game.application.usecase;

import blackjack.game.domain.Game;
import reactor.core.publisher.Mono;

public interface FinishGame {
    Mono<Game> shouldFinish(Game game);
    Mono<Game> finish(String gameId);
}
