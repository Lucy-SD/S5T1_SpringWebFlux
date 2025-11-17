package blackjack.module.game.application.usecase;

import blackjack.module.game.domain.entity.Game;
import reactor.core.publisher.Mono;

public interface FinishGame {
    Mono<Boolean> shouldFinish(Game game);
    Mono<Game> finish(Game game);
}
