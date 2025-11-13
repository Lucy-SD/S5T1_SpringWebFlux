package blackjack.aahhrefact.module.game.application.usecase;

import blackjack.aahhrefact.module.game.domain.entity.Game;
import reactor.core.publisher.Flux;

public interface GetGameHistory {
    Flux<Game> getGameHistory(Long playerId);
}
