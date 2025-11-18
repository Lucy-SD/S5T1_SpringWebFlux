package blackjack.module.player.infrastructure.controller;

import blackjack.module.player.application.adapter.mapper.PlayerMapper;
import blackjack.module.player.application.dto.request.UpdatePlayerRequest;
import blackjack.module.player.application.dto.response.PlayerResponse;
import blackjack.module.player.application.usecase.DeletePlayer;
import blackjack.module.player.application.usecase.FindOrCreatePlayer;
import blackjack.module.player.application.usecase.UpdatePlayerName;
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
public class PlayerController {

    private final FindOrCreatePlayer getPlayer;
    private final UpdatePlayerName updateName;
    private final DeletePlayer deletePlayer;
    private final PlayerMapper mapper;

    @GetMapping("/{id}")
    public Mono<PlayerResponse> getPlayerById(@PathVariable @Positive Long id) {
        return getPlayer.findPlayerById(id)
                .map(mapper::toResponse);
    }

    @GetMapping("/name/{name}")
    public Mono<PlayerResponse> getPlayerByName(@PathVariable @NotBlank String name){
        return getPlayer.findOrCreatePlayerByName(name)
                .map(mapper::toResponse);
    }

    @PutMapping("/{id}")
    public Mono<PlayerResponse> updatePlayersName(
            @PathVariable @Positive Long id,
            @Valid @RequestBody UpdatePlayerRequest request) {
        return updateName.updateName(id, request.newName())
                .map(mapper::toResponse);

    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePlayer(@PathVariable @Positive Long id) {
        return deletePlayer.delete(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
