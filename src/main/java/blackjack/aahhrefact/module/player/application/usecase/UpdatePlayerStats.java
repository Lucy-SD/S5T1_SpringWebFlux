package blackjack.aahhrefact.module.player.application.usecase;

import blackjack.aahhrefact.module.player.domain.entity.Player;
import reactor.core.publisher.Mono;

public interface UpdatePlayerStats {
    Mono<Player> incrementWins(Long playerId);
    Mono<Player> incrementLosses(Long playerId);
    Mono<Player> incrementPushes(Long playerId);
}
