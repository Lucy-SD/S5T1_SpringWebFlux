package blackjack.aahhrefact.module.player.application.usecase;

import blackjack.aahhrefact.module.player.domain.entity.Player;
import reactor.core.publisher.Mono;

public interface GetPlayer {
    Mono<Player> getPlayer(Long id);
}
