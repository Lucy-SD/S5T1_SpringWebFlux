package blackjack.game.repository;

import blackjack.game.GameEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface GameRepository extends ReactiveMongoRepository<GameEntity, String> {

    Flux<GameEntity> findByPlayerId(Long playerId);
}
