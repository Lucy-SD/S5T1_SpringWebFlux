package blackjack.game.service;

import blackjack.game.entity.GameEntity;
import blackjack.game.model.GameStatus;
import blackjack.game.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReadGameService {
    private final GameRepository gameRepository;

    public Mono<GameEntity> findGameById(String gameId) {
        return gameRepository.findById(gameId);
    }

    public Mono<Boolean> hasActiveGame(Long playerId) {
        return gameRepository.findByPlayerId(playerId)
                .filter(game -> game.getStatus() == GameStatus.ACTIVE)
                .hasElements();
    }

    public Mono<Long> getFinishedGamesCount(Long playerId) {
        return gameRepository.findByPlayerId(playerId)
                .filter(game -> game.getStatus() == GameStatus.FINISHED)
                .count();
    }

    public Flux<GameEntity> getPlayerGameHistory(Long playerId) {
        return gameRepository.findByPlayerId(playerId)
                .filter(game -> game.getStatus() == GameStatus.FINISHED);
    }
}
