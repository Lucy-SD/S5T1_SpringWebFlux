package blackjack.game.application.usercase;

import blackjack.game.domain.Game;
import reactor.core.publisher.Mono;

public interface CreateGame {
    Mono<Game> execute (String playerName);
}
