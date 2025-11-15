package blackjack.module.player.application.usecase;

import blackjack.module.player.domain.entity.Player;
import reactor.core.publisher.Mono;

public interface FindOrCreatePlayer {
    Mono<Player> findPlayerById(Long id);
    Mono<Player> findOrCreatePlayerByName(String name);
}
