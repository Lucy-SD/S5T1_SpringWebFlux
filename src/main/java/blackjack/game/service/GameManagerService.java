package blackjack.game.service;

import blackjack.exception.GameException;
import blackjack.game.domain.Game;
import blackjack.game.domain.GameResult;
import blackjack.game.domain.GameStatus;
import blackjack.game.mapper.GameMapper;
import blackjack.game.repository.GameRepository;
import blackjack.gamer.domain.Player;
import blackjack.gamer.mapper.PlayerMapper;
import blackjack.gamer.infrastructure.persistence.PlayerRepository;
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

    public Mono<Game> createNewGame(String playerName) {
        return playerRepository.findByName(playerName)
                .switchIfEmpty(
                        Mono.defer(() -> {
                            Player player = new Player(playerName);
                            Player Player = playerMapper.toEntity(player);
                            return playerRepository.save(Player);
                        })
                )
                .flatMap(Player -> {
                    return playGameService.startNewGame(playerName)
                            .map(gameState -> gameMapper.toGame(gameState, Player.getId()));
                })
                .flatMap(gameRepository::save);
    }

    public Mono<Game> playerHit(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameException("No se encontró la partida con ID: "
                        + gameId + ".")))
                .flatMap(Game ->
                        gameMapper.toGameState(Game)
                                .flatMap(playGameService::playerHit)
                                .map(updatedGameState -> {
                                    Game updatedEntity = gameMapper.toGame(updatedGameState, Game.getPlayerId());
                                    updatedEntity.setId(Game.getId());
                                    return updatedEntity;
                                })
                )
                .flatMap(gameRepository::save);
    }

    public Mono<Game> playerStand(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameException("No se encontró la partida con ID: "
                        + gameId +".")))
                .flatMap(Game ->
                        gameMapper.toGameState(Game)
                                .flatMap(playGameService::playerStand)
                                .map(updatedGameState -> {
                                    Game updatedEntity = gameMapper.toGame(updatedGameState, Game.getPlayerId());
                                    updatedEntity.setId(Game.getId());
                                    updatedEntity.setDealerFirstCardHidden(false);
                                    return updatedEntity;
                                })
                )
                .flatMap(gameRepository::save);
    }

    public Mono<Game> finishGame(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameException("No se encontró la partida con ID: "
                        + gameId +".")))
                .flatMap(Game ->
                        gameMapper.toGameState(Game)
                                .flatMap(playGameService::findOutWinner)
                                .flatMap(gameResult ->
                                        updatePlayerStats(Game.getPlayerId(), gameResult)
                                                .then(Mono.just(Game))
                                )
                )
                .flatMap(Game -> {
                    Game.setStatus(GameStatus.FINISHED);
                    Game.setDealerFirstCardHidden(false);
                    return gameRepository.save(Game);
                });
    }

    private Mono<Void> updatePlayerStats(Long playerId, GameResult gameResult) {
        return playerRepository.findById(playerId)
                .flatMap(Player -> {
                    switch (gameResult.winner()) {
                        case PLAYER -> Player.setGamesWon(Player.getGamesWon() + 1);
                        case DEALER -> Player.setGamesLost(Player.getGamesLost() + 1);
                        case PUSH -> Player.setGamesPushed(Player.getGamesPushed() + 1);
                    }
                    return playerRepository.save(Player);
                })
                .then();
    }

    public Mono<Void> deleteGame(String gameId) {
        return gameRepository.deleteById(gameId);
    }
}
