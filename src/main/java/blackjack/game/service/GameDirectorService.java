package blackjack.game.service;

import blackjack.game.domain.Game;
import blackjack.game.model.PlayerAction;
import reactor.core.publisher.Mono;

public class GameDirectorService {
    public Mono<Game> processPlayerAction(String gameId, PlayerAction action) {
        return null;
    }
}
