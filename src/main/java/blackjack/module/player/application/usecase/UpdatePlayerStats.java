package blackjack.module.player.application.usecase;

import blackjack.module.player.domain.entity.Player;
import reactor.core.publisher.Mono;

public interface UpdatePlayerStats {
    Mono<Player> incrementWins(Long playerId);
    Mono<Player> incrementLosses(Long playerId);
    Mono<Player> incrementPushes(Long playerId);
}
