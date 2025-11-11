package blackjack.game.application.usecase;

import blackjack.aahhrefact.module.game.domain.entity.Game;
import reactor.core.publisher.Mono;

public interface Hit {
    Mono<Game> hit(String gameId);
}
