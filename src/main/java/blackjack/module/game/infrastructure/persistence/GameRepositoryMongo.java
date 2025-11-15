package blackjack.module.game.infrastructure.persistence;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface GameRepositoryMongo extends ReactiveMongoRepository<GameMongoEntity,String> {

    @Query("{ 'playerId': ?0 }")
    Flux<GameMongoEntity> findByPlayerId(Long playerId);
}
