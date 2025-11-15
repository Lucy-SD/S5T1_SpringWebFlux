package blackjack.aahhrefact.module.player.application.usecase;

import blackjack.aahhrefact.module.player.domain.entity.Player;
import reactor.core.publisher.Mono;

public interface GetPlayer {
    Mono<Player> getPlayerById(Long id);
    Mono<Player> getPlayerByName(String name);
}
