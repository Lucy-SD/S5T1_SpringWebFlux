package blackjack.module.player.application.usecase;

import blackjack.module.player.domain.entity.Player;
import reactor.core.publisher.Mono;

public interface UpdatePlayerName {
    public Mono<Player> updateName(Long id, String name);
}
