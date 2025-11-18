package blackjack.module.player.infrastructure.controller;

import blackjack.module.player.application.adapter.mapper.PlayerMapper;
import blackjack.module.player.application.dto.request.UpdatePlayerRequest;
import blackjack.module.player.application.dto.response.PlayerResponse;
import blackjack.module.player.application.usecase.DeletePlayer;
import blackjack.module.player.application.usecase.FindOrCreatePlayer;
import blackjack.module.player.application.usecase.UpdatePlayerName;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
@Tag(name = "Jugador", description = "API's para gestion de los jugadores de BlackJack.")
public class PlayerController {

    private final FindOrCreatePlayer getPlayer;
    private final UpdatePlayerName updateName;
    private final DeletePlayer deletePlayer;
    private final PlayerMapper mapper;

    @Operation(summary = "Obtener jugador por ID.", description = "Obtiene los detalles de un jugador por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jugador encontrado."),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado.")
    })
    @GetMapping("/{id}")
    public Mono<PlayerResponse> getPlayerById(
            @Parameter(description = "ID del jugador.", required = true, example = "7")
            @PathVariable @Positive Long id) {
        return getPlayer.findPlayerById(id)
                .map(mapper::toResponse);
    }

    @Operation(summary = "Obtener o crear jugador por nombre.", description = "Busca un jugador por su nombre, y si no" +
            " existe, lo crea.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jugador encontrado o creado."),
    })
    @GetMapping("/name/{name}")
    public Mono<PlayerResponse> getPlayerByName(
            @Parameter(description = "Nombre del jugador.", required = true, example = "Pepito")
            @PathVariable @NotBlank String name){
        return getPlayer.findOrCreatePlayerByName(name)
                .map(mapper::toResponse);
    }

    @Operation(summary = "Actualizar nombre de un jugador.", description = "Busca un jugador por su ID, y actualiza su nombre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre actualizado."),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado.")
    })
    @PutMapping("/{id}")
    public Mono<PlayerResponse> updatePlayersName(
            @Parameter(description = "ID del jugador.", required = true, example = "7")
            @PathVariable @Positive Long id,
            @Valid @RequestBody UpdatePlayerRequest request) {
        return updateName.updateName(id, request.newName())
                .map(mapper::toResponse);

    }

    @Operation(summary = "Eliminar jugador por ID.", description = "Borra un jugador existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Jugador eliminado."),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado.")
    })
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePlayer(
            @Parameter(description = "ID del jugador.", required = true, example = "7")
            @PathVariable @Positive Long id) {
        return deletePlayer.delete(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
