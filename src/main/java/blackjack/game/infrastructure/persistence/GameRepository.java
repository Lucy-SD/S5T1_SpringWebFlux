package blackjack.game.infrastructure.persistence;

import blackjack.aahhrefact.module.game.domain.entity.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface GameRepository extends ReactiveMongoRepository<Game, String> {

    Flux<Game> findByPlayerId(Long playerId);
}
