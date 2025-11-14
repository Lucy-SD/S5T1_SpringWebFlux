package blackjack.aahhrefact.module.player.application.usecase;

import blackjack.aahhrefact.module.player.domain.entity.Player;
import reactor.core.publisher.Flux;

public interface GetRanking {
    Flux<Player> getRanking();
}
