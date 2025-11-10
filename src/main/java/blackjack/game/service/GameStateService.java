package blackjack.game.service;

import blackjack.exception.GameException;
import blackjack.game.domain.Game;
import blackjack.game.model.GameState;
import blackjack.game.model.PlayerAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GameStateService {
    private final PlayGameService playGameService;
    private final GameMapper mapper;

    public Mono<Game> processAction(Game Game, PlayerAction action) {
        return mapper.toGameState(Game)
                .flatMap(gameState -> validateAndProcessAction(gameState, action))
                .map(updatedGameState -> {
                    Game.updateFromGameState(updatedGameState);
                    return Game;
                });
    }

    private Mono<GameState> validateAndProcessAction(GameState gameState, PlayerAction action) {
        switch (action) {
            case HIT -> {
                if (gameState.getPlayer().getScore() >= 21) {
                    return Mono.error(new GameException("El jugador ya no puede pedir cartas."));
                }
                return playGameService.playerHit(gameState);
            }
            case STAND -> {
                return playGameService.playerStand(gameState);
            }
            default -> {
                return Mono.error(new GameException("Acción no válida: " + action + "."));
            }
        }
    }
}
