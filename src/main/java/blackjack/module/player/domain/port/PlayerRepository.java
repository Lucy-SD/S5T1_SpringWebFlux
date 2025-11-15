package blackjack.module.player.domain.port;

import blackjack.module.player.domain.entity.Player;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerRepository {
    Mono<Player> findByName(String name);
    Mono<Boolean> existsByName(String name);
    Mono<Player> findById(Long id);
    Mono<Player> save(Player player);
    Mono<Void> delete(Player player);
    Flux<Player> findAll();
}
