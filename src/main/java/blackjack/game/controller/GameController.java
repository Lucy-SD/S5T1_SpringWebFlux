package blackjack.game.controller;

import blackjack.exception.GameException;
import blackjack.game.dto.request.CreateGameRequest;
import blackjack.game.dto.response.GameResponse;
import blackjack.game.mapper.GameMapper;
import blackjack.game.mapper.GameResponseMapper;
import blackjack.game.service.GameManagerService;
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
    private final GameManagerService gameManager;
    private final GameMapper mapper;
    private final GameResponseMapper responseMapper;

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
}
