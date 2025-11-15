package blackjack.module.game.application.usecase;

import blackjack.module.game.domain.entity.Game;
import reactor.core.publisher.Mono;

public interface GetGameById {
    Mono<Game> getGameById(String id);
}
