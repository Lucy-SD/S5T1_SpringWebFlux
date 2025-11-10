package blackjack.game.controller;

import blackjack.exception.GameException;
import blackjack.game.domain.Game;
import blackjack.game.dto.request.CreateGameRequest;
import blackjack.game.dto.request.PlayRequest;
import blackjack.game.dto.response.GameResponse;
import blackjack.game.dto.response.PlayResponse;
import blackjack.game.mapper.GameMapper;
import blackjack.game.mapper.GameResponseMapper;
import blackjack.game.domain.GameResult;
import blackjack.game.model.GameState;
import blackjack.game.domain.GameStatus;
import blackjack.game.service.GameDirectorService;
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
    private final GameDirectorService gameDirector;
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
                .flatMap(Game ->
                        mapper.toGameState(Game)
                                .map(gameState -> responseMapper.toGameResponse(
                                        gameState,
                                        Game.getId(),
                                        Game.getStatus()
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
                .flatMap(Game -> mapper.toGameState(Game)
                        .map(gameState -> responseMapper.toGameResponse(
                                gameState, gameId, Game.getStatus()
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
                .flatMap(Game -> {
                    if (Game.getStatus() != GameStatus.ACTIVE) {
                        return Mono.error(new GameException("Partida finalizada."));
                    }
                    return Mono.just(Game);
                })
                .flatMap(Game -> gameDirector.processPlayerAction(gameId, request.action()))
                .flatMap(this::processGameResult)
                .map(ResponseEntity::ok)
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.badRequest().build())
                );
    }

    private Mono<PlayResponse> processGameResult(Game Game) {
        return mapper.toGameState(Game)
                .flatMap(gameState -> {
                    if (Game.getStatus() == GameStatus.FINISHED) {
                        return playGame.findOutWinner(gameState)
                                .map(gameResult -> new PlayResponse(
                                        GameStatus.FINISHED,
                                        responseMapper.toGameResponse(gameState, Game.getId(), GameStatus.FINISHED),
                                        gameResult
                                ));
                    } else {
                        return Mono.just(new PlayResponse(
                                GameStatus.ACTIVE,
                                responseMapper.toGameResponse(gameState, Game.getId(), GameStatus.ACTIVE),
                                null
                        ));
                    }
                });
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