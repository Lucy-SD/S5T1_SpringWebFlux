package blackjack.gamer.repository;

import blackjack.gamer.entity.PlayerEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PlayerRepository extends ReactiveCrudRepository<PlayerEntity, Long> {
    Mono<PlayerEntity> findByName(String name);
    Mono<Boolean> existsByName(String name);
}
