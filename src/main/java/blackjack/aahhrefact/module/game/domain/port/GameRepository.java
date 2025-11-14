package blackjack.aahhrefact.module.game.domain.port;

import blackjack.aahhrefact.module.game.domain.entity.Game;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameRepository {
    Mono<Game> findById(String id);
    Mono<Game> save(Game game);
    Flux<Game> findByPlayerId(Long playerId);
    Mono<Void> deleteById(String id);
}
