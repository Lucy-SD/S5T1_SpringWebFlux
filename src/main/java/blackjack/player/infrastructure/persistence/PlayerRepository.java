package blackjack.player.infrastructure.persistence;

import blackjack.player.domain.Player;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PlayerRepository extends ReactiveCrudRepository<Player, Long> {
    Mono<Player> findByName(String name);
    Mono<Boolean> existsByName(String name);
}
