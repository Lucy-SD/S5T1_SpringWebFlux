package blackjack.module.game.infrastructure.controller;

import blackjack.module.game.application.adapter.mapper.GameResponseMapper;
import blackjack.module.game.application.dto.request.CreateGameRequest;
import blackjack.module.game.application.dto.response.GameResponse;
import blackjack.module.game.application.dto.response.PlayResponse;
import blackjack.module.game.application.usecase.*;
import blackjack.module.player.application.usecase.FindOrCreatePlayer;
import blackjack.shared.exception.GameException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
@Tag(name = "Game", description = "API's to manage BlackJack game.")
public class GameController {

    private final CreateGame createGame;
    private final GetGameById getGameById;
    private final FindOrCreatePlayer playerFinder;
    private final Hit hit;
    private final Stand stand;
    private final DeleteGame deleteGame;
    private final GameResponseMapper mapper;

    @Operation(summary = "New game.", description = "Creates a new Blackjack game from a players name." +
            " If the player doesn't exist, it creates a new player and a new game.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New game created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request.")
    })
    @PostMapping("/new")
    public Mono<ResponseEntity<GameResponse>> newGame(@Valid @RequestBody CreateGameRequest request) {
        return createGame.create(request.playerName())
                .map(game -> mapper.toResponse(game, request.playerName()))
                .map(response -> ResponseEntity.status(201).body(response))
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.badRequest().build())
                );
    }

    @Operation(summary = "Search game.", description = "Research's a BlackJack game by it's ID and returns " +
            " the game state.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game found."),
            @ApiResponse(responseCode = "404", description = "Game not found.")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<GameResponse>> getGame(
            @Parameter(description = "Game ID.", required = true, example = "691b5d0c6738ea08096c1e2a")
            @PathVariable @NotBlank String id) {
        return getGameById.getGameById(id)
                .flatMap(game -> playerFinder.findPlayerById(game.getPlayerId())
                        .map(player -> mapper.toResponse(game, player.getName())))
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @Operation(summary = "Player hit's.", description = "Player ask's for an additional card.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hit success."),
            @ApiResponse(responseCode = "400", description = "Hit not allowed or game not found.")
    })
    @PostMapping("/{id}/hit")
    public Mono<ResponseEntity<PlayResponse>> hit(
            @Parameter(description = "Game ID.", required = true, example = "691b5d0c6738ea08096c1e2a")
            @PathVariable @NotBlank String id) {
        return hit.hit(id)
                .flatMap(game -> playerFinder.findPlayerById(game.getPlayerId())
                        .map(player -> mapper.toPlayResponse(game, player.getName())))
                .map(ResponseEntity::ok)
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.badRequest().build())
                );
    }

    @Operation(summary = "Player stand's.", description = "Player stand's and the turn goes to the dealer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game found."),
            @ApiResponse(responseCode = "400", description = "Game not found.")
    })
    @PostMapping("/{id}/stand")
    public Mono<ResponseEntity<PlayResponse>> stand(
            @Parameter(description = "Game ID.", required = true, example = "691b5d0c6738ea08096c1e2a")
            @PathVariable @NotBlank String id) {
        return stand.stand(id)
                .flatMap(game -> playerFinder.findPlayerById(game.getPlayerId())
                        .map(player -> mapper.toPlayResponse(game, player.getName())))
                .map(ResponseEntity::ok)
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.badRequest().build())
                );
    }

    @Operation(summary = "Delete game.", description = "Delete's an existing game from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Game deleted."),
            @ApiResponse(responseCode = "404", description = "Game not found.")
    })
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteGame(
            @Parameter(description = "Game ID.", required = true, example = "691b5d0c6738ea08096c1e2a")
            @PathVariable @NotBlank String id) {
        return deleteGame.delete(id)
                .thenReturn(ResponseEntity.noContent().<Void>build())
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.notFound().build()));
    }
}



