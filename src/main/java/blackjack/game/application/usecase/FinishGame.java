package blackjack.game.application.usecase;

import blackjack.aahhrefact.module.game.domain.entity.Game;
import reactor.core.publisher.Mono;

public interface FinishGame {
    Mono<Game> shouldFinish(Game game);
    Mono<Game> finish(String gameId);
}
