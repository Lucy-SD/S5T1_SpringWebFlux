package blackjack.module.game.domain;

import blackjack.module.game.domain.entity.Game;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameRepository {
    Mono<Game> findById(String id);
    Mono<Game> save(Game game);
    Flux<Game> findByPlayerId(Long playerId);
    Mono<Void> deleteById(String id);
}
