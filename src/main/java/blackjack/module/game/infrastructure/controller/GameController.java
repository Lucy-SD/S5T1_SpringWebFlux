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
@Tag(name = "Juego", description = "API's para gestion de las partidas de BlackJack.")
public class GameController {

    private final CreateGame createGame;
    private final GetGameById getGameById;
    private final FindOrCreatePlayer playerFinder;
    private final Hit hit;
    private final Stand stand;
    private final DeleteGame deleteGame;
    private final GameResponseMapper mapper;

    @Operation(summary = "Crear nueva partida.", description = "Crea una nueva partida de BlackJack para un jugador" +
            "identificado por nombre. Si el jugador no existe en base de datos, crea uno nuevo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Partida creada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida.")
    })
    @PostMapping("/new")
    public Mono<ResponseEntity<GameResponse>> newGame(@Valid @RequestBody CreateGameRequest request) {
        return createGame.create(request.playerName())
                .map(game -> mapper.toResponse(game, request.playerName()))
                .map(ResponseEntity::ok)
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.badRequest().build())
                );
    }

    @Operation(summary = "Buscar partida.", description = "Busca una partida de BlackJack por ID de la partida" +
            " y devuelve el estado de la misma.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Partida encontrada."),
            @ApiResponse(responseCode = "404", description = "Partida no encontrada.")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<GameResponse>> getGame(
            @Parameter(description = "ID de la partida.", required = true, example = "691b5d0c6738ea08096c1e2a")
            @PathVariable @NotBlank String id) {
        return getGameById.getGameById(id)
                .flatMap(game -> playerFinder.findPlayerById(game.getPlayerId())
                        .map(player -> mapper.toResponse(game, player.getName())))
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @Operation(summary = "Pedir carta ('hit').", description = "El jugador pide una carta adicional.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Acción exitosa."),
            @ApiResponse(responseCode = "400", description = "Acción no permitida o juego no encontrado.")
    })
    @PostMapping("/{id}/hit")
    public Mono<ResponseEntity<PlayResponse>> hit(
            @Parameter(description = "ID de la partida.", required = true, example = "691b5d0c6738ea08096c1e2a")
            @PathVariable @NotBlank String id) {
        return hit.hit(id)
                .flatMap(game -> playerFinder.findPlayerById(game.getPlayerId())
                        .map(player -> mapper.toPlayResponse(game, player.getName())))
                .map(ResponseEntity::ok)
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.badRequest().build())
                );
    }

    @Operation(summary = "Plantarse ('stand').", description = "El jugador se planta y se pasa el turno al dealer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Partida encontrada."),
            @ApiResponse(responseCode = "400", description = "Partida no encontrada.")
    })
    @PostMapping("/{id}/stand")
    public Mono<ResponseEntity<PlayResponse>> stand(
            @Parameter(description = "ID de la partida.", required = true, example = "691b5d0c6738ea08096c1e2a")
            @PathVariable @NotBlank String id) {
        return stand.stand(id)
                .flatMap(game -> playerFinder.findPlayerById(game.getPlayerId())
                        .map(player -> mapper.toPlayResponse(game, player.getName())))
                .map(ResponseEntity::ok)
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.badRequest().build())
                );
    }

    @Operation(summary = "Eliminar partida.", description = "Borra una partida existente de base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Partida eliminada."),
            @ApiResponse(responseCode = "404", description = "Partida no encontrada.")
    })
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteGame(
            @Parameter(description = "ID de la partida.", required = true, example = "691b5d0c6738ea08096c1e2a")
            @PathVariable @NotBlank String id) {
        return deleteGame.delete(id)
                .thenReturn(ResponseEntity.noContent().<Void>build())
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.notFound().build()));
    }
}



