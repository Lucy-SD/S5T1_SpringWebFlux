package blackjack.aahhrefact.module.game.application.usecase;

import blackjack.aahhrefact.module.game.domain.entity.Game;
import reactor.core.publisher.Mono;

public interface FinishGame {
    Mono<Boolean> shouldFinish(Game game);
    Mono<Game> finish(String gameId);
}
