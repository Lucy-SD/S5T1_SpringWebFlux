package blackjack.game.controller;

import blackjack.exception.GameException;
import blackjack.game.dto.request.CreateGameRequest;
import blackjack.game.dto.request.PlayRequest;
import blackjack.game.dto.response.GameResponse;
import blackjack.game.dto.response.PlayResponse;
import blackjack.game.mapper.GameMapper;
import blackjack.game.mapper.GameResponseMapper;
import blackjack.game.model.GameResult;
import blackjack.game.model.GameState;
import blackjack.game.model.GameStatus;
import blackjack.game.model.PlayerAction;
import blackjack.game.service.GameManagerService;
import blackjack.game.service.PlayGameService;
import blackjack.game.service.ReadGameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {
    private final ReadGameService readGame;
    private final PlayGameService playGame;
    private final GameManagerService gameManager;
    private final GameMapper mapper;
    private final GameResponseMapper responseMapper;

    private record Tuple(GameState gameState, GameResult gameResult, GameStatus status) {
        static Tuple of(GameState gameState, GameResult gameResult, GameStatus status) {
            return new Tuple(gameState, gameResult, status);
        }
    }

    @PostMapping("/new")
    public Mono<ResponseEntity<GameResponse>> createGame(@Valid @RequestBody CreateGameRequest request) {
        return gameManager.createNewGame(request.playerName())
                .flatMap(gameEntity ->
                        mapper.toGameState(gameEntity)
                                .map(gameState -> responseMapper.toGameResponse(
                                        gameState,
                                        gameEntity.getId(),
                                        gameEntity.getStatus()
                                ))
                )
                .map(gameResponse ->
                        ResponseEntity.status(HttpStatus.CREATED).body(gameResponse)
                )
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.badRequest().build())
                );
    }

    @GetMapping("/{gameId}")
    public Mono<ResponseEntity<GameResponse>> getGame(@PathVariable String gameId) {
        return readGame.findGameById(gameId)
                .flatMap(gameEntity -> mapper.toGameState(gameEntity)
                        .map(gameState -> responseMapper.toGameResponse(
                                gameState, gameId, gameEntity.getStatus()
                        ))
                        .map(ResponseEntity::ok)
                )
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.notFound().build())
                );
    }

    @PostMapping("/{gameId}/play")
    public Mono<ResponseEntity<PlayResponse>> playGame(
            @PathVariable String gameId,
            @Valid @RequestBody PlayRequest request) {
        return readGame.findGameById(gameId)
                .switchIfEmpty(Mono.error(new GameException("No se encontró la partida.")))
                .flatMap(gameEntity -> {
                    if (gameEntity.getStatus() != GameStatus.ACTIVE) {
                        return Mono.error(new GameException("Partida finalizada."));
                    }
                    return mapper.toGameState(gameEntity);
                })
                .flatMap(gameState -> {
                    if (gameState.getPlayer().getScore() >= 21 && request.action() == PlayerAction.HIT) {
                        return Mono.error(new GameException("El jugador ya no puede pedir cartas."));
                    }
                    switch (request.action()) {
                        case HIT -> {
                            return gameManager.playerHit(gameId);
                        }
                        case STAND -> {
                            return gameManager.playerStand(gameId);
                        }
                        default -> {
                            return Mono.error(new GameException("Acción no válida."));
                        }
                    }
                })
                .flatMap(gameEntity ->
                        mapper.toGameState(gameEntity)
                                .flatMap(gameState ->
                                        playGame.findOutWinner(gameState)
                                                .flatMap(gameResult -> {
                                                    boolean gameFinished = gameResult.winner() != null;
                                                    GameStatus status = gameFinished ? GameStatus.FINISHED : GameStatus.ACTIVE;
                                                    if (gameFinished) {
                                                        return gameManager.finishGame(gameEntity.getId())
                                                                .then(Mono.just(Tuple.of(gameState, gameResult, status)));
                                                    } else {
                                                        return Mono.just(Tuple.of(gameState, gameResult, status));
                                                    }
                                                })
                                )
                )
                .map(tuple -> {
                    GameResponse gameResponse = responseMapper.toGameResponse(
                            tuple.gameState(), gameId, tuple.status()
                    );
                    return new PlayResponse(tuple.status(), gameResponse, tuple.status() == GameStatus.FINISHED ?
                            tuple.gameResult() : null);
                })
                .map(ResponseEntity::ok)
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.badRequest().build())
                );
    }

    @DeleteMapping("/{gameId}")
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable String gameId) {
        return gameManager.deleteGame(gameId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.notFound().build())
                );
    }
}