package blackjack.game.application.usecase;

import blackjack.aahhrefact.module.game.domain.entity.Game;
import reactor.core.publisher.Mono;

public interface DealersTurn {
    Mono<Game> play(Game game);
}
