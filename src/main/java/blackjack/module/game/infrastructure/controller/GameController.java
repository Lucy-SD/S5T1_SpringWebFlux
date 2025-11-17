package blackjack.module.game.infrastructure.controller;

import blackjack.module.game.application.adapter.mapper.GameResponseMapper;
import blackjack.module.game.application.dto.request.CreateGameRequest;
import blackjack.module.game.application.dto.response.GameResponse;
import blackjack.module.game.application.dto.response.PlayResponse;
import blackjack.module.game.application.usecase.*;
import blackjack.module.game.domain.entity.Game;
import blackjack.module.game.domain.valueObject.GameStatus;
import blackjack.module.player.application.usecase.FindOrCreatePlayer;
import blackjack.shared.exception.GameException;
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

    private final CreateGame createGame;
    private final GetGameById getGameById;
    private final FindOrCreatePlayer playerFinder;
    private final Hit hit;
    private final Stand stand;
    private final DeleteGame deleteGame;
    private final GameResponseMapper mapper;

    @PostMapping("/new")
    public Mono<ResponseEntity<GameResponse>> newGame(@Valid @RequestBody CreateGameRequest request) {
        return createGame.create(request.playerName())
                .flatMap(this::mapGameToResponse)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.badRequest().build())
                );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<GameResponse>> getGame(@PathVariable String id) {
        return getGameById.getGameById(id)
                .flatMap(this::mapGameToResponse)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/{id}/hit")
    public Mono<ResponseEntity<PlayResponse>> hit(@PathVariable String id) {
        return hit.hit(id)
                .flatMap(this::mapGameToPlayResponse)
                .map(ResponseEntity::ok)
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.badRequest().build())
                );
    }

    @PostMapping("/{id}/stand")
    public Mono<ResponseEntity<PlayResponse>> stand(@PathVariable String id) {
        return stand.stand(id)
                .flatMap(this::mapGameToPlayResponse)
                .map(ResponseEntity::ok)
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.badRequest().build())
                );
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable String id) {
        return deleteGame.delete(id)
                .thenReturn(ResponseEntity.noContent().<Void>build())
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.notFound().build()));
    }

    private Mono<GameResponse> mapGameToResponse(Game game) {
        return playerFinder.findPlayerById(game.getPlayerId())
                .map(player -> mapper.toResponse(game, player.getName()));
    }

    private Mono<PlayResponse> mapGameToPlayResponse(Game game) {
        return mapGameToResponse(game)
                .map(gameResponse -> {
                    if (game.getStatus() == GameStatus.FINISHED && game.getResult() != null) {
                        return new PlayResponse(
                                GameStatus.FINISHED,
                                gameResponse,
                                game.getResult()
                        );
                    } else {
                        return new PlayResponse(
                                game.getStatus(),
                                gameResponse,
                                null
                        );
                    }
                });
    }
}



