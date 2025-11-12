package blackjack.aahhrefact.module.game.application.usecase;

import blackjack.aahhrefact.module.game.domain.entity.Game;
import reactor.core.publisher.Mono;

public interface CreateGame {
    Mono<Game> create(String playerName);
}
