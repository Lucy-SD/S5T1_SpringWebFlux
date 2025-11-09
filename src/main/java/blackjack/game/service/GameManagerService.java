package blackjack.game.service;

import blackjack.exception.GameException;
import blackjack.game.GameEntity;
import blackjack.game.mapper.GameMapper;
import blackjack.game.repository.GameRepository;
import blackjack.gamer.Player;
import blackjack.gamer.entity.PlayerEntity;
import blackjack.gamer.mapper.PlayerMapper;
import blackjack.gamer.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GameManagerService {
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final PlayGameService playGameService;

    public Mono<GameEntity> createNewGame(String playerName) {
        return playerRepository.findByName(playerName)
                .switchIfEmpty(
                        Mono.defer(() -> {
                            Player player = new Player(playerName);
                            PlayerEntity playerEntity = playerMapper.toEntity(player);
                            return playerRepository.save(playerEntity);
                        })
                )
                .flatMap(playerEntity -> {
                    return playGameService.startNewGame(playerName)
                            .map(gameState -> gameMapper.toGameEntity(gameState, playerEntity.getId()));
                })
                .flatMap(gameRepository::save);
    }

    public Mono<GameEntity> playerHit(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameException("No se encontró la partida con ID: "
                        + gameId)))
                .flatMap(gameEntity ->
                        gameMapper.toGameState(gameEntity)
                                .flatMap(playGameService::playerHit)
                                .map(updatedGameState -> {
                                    GameEntity updatedEntity = gameMapper.toGameEntity(updatedGameState, gameEntity.getPlayerId());
                                    updatedEntity.setId(gameEntity.getId());
                                    return updatedEntity;
                                })
                )
                .flatMap(gameRepository::save);
    }
}
