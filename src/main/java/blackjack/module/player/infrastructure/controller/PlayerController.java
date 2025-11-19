package blackjack.module.player.infrastructure.controller;

import blackjack.module.player.application.adapter.mapper.PlayerMapper;
import blackjack.module.player.application.dto.request.UpdatePlayerRequest;
import blackjack.module.player.application.dto.response.PlayerResponse;
import blackjack.module.player.application.usecase.DeletePlayer;
import blackjack.module.player.application.usecase.FindOrCreatePlayer;
import blackjack.module.player.application.usecase.UpdatePlayerName;
import blackjack.shared.exception.GameException;
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
@Tag(name = "Player", description = "API's to manage BlackJack player's.")
public class PlayerController {

    private final FindOrCreatePlayer getPlayer;
    private final UpdatePlayerName updateName;
    private final DeletePlayer deletePlayer;
    private final PlayerMapper mapper;

    @Operation(summary = "Get player by ID.", description = "Get's player's details by it's ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player found."),
            @ApiResponse(responseCode = "404", description = "Player not found.")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PlayerResponse>> getPlayerById(
            @Parameter(description = "Player's ID.", required = true, example = "7")
            @PathVariable @Positive Long id) {
        return getPlayer.findPlayerById(id)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.notFound().build()));
    }

    @Operation(summary = "Get or create a player by name.", description = "Search's a player by name, and if it doesn't " +
            "exists, it creates a new one.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player found or created."),
    })
    @GetMapping("/name/{name}")
    public Mono<ResponseEntity<PlayerResponse>> getPlayerByName(
            @Parameter(description = "Player's name.", required = true, example = "Jack.")
            @PathVariable @NotBlank String name){
        return getPlayer.findOrCreatePlayerByName(name)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Update Player's name.", description = "Searches a player by it's ID, and updates it's name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Name updated."),
            @ApiResponse(responseCode = "404", description = "Player not found.")
    })
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PlayerResponse>> updatePlayersName(
            @Parameter(description = "Player's ID.", required = true, example = "7")
            @PathVariable @Positive Long id,
            @Valid @RequestBody UpdatePlayerRequest request) {
        return updateName.updateName(id, request.newName())
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.notFound().build()));

    }

    @Operation(summary = "Delete player by ID.", description = "Delete's a player by it's ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Player deleted."),
            @ApiResponse(responseCode = "404", description = "Player not found.")
    })
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePlayer(
            @Parameter(description = "Player's ID.", required = true, example = "7")
            @PathVariable @Positive Long id) {
        return deletePlayer.delete(id)
                .thenReturn(ResponseEntity.noContent().<Void>build())
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.notFound().build()));
    }
}
