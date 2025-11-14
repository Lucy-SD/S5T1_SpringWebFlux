package blackjack.aahhrefact.module.player.application.usecase;

import blackjack.aahhrefact.module.player.domain.entity.Player;
import reactor.core.publisher.Mono;

public interface FindOrCreatePlayer {
    Mono<Player> findPlayerById(Long id);
    Mono<Player> findOrCreatePlayerByName(String name);
}
