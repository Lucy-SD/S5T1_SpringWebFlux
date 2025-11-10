package blackjack.game.service;

import blackjack.game.domain.GameEntity;
import blackjack.game.model.PlayerAction;
import reactor.core.publisher.Mono;

public class GameDirectorService {
    public Mono<GameEntity> processPlayerAction(String gameId, PlayerAction action) {
        return null;
    }
}
