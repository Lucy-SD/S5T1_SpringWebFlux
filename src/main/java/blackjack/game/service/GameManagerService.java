package blackjack.game.service;

import blackjack.game.domain.Game;
import blackjack.game.infrastructure.persistence.GameRepository;
import blackjack.player.application.service.PlayerFinder;
import blackjack.player.infrastructure.persistence.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class GameManagerService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final PlayerFinder playerFinder;

    public Mono<Game> createNewGame(String playerName) {
        return playerFinder.findOrCreate(playerName)
                .flatMap(player -> {
                    Game newGame = Game.builder()
                            .playerId(player.getId())
                            .playerHand(new ArrayList<>())
                            .dealerHand(new ArrayList<>())
                            .build();
                    return gameRepository.save(newGame);
                });
    }

    public Mono<Game> playerHit(String gameId) {
        return Mono.error(new UnsupportedOperationException("Por implementar"));
    }

    public Mono<Game> playerStand(String gameId) {
        return Mono.error(new UnsupportedOperationException("Por implementar"));
    }

    public Mono<Game> finishGame(String gameId) {
        return Mono.error(new UnsupportedOperationException("Por implementar"));
    }

    public Mono<Void> deleteGame(String gameId) {
        return gameRepository.deleteById(gameId);
    }

}
