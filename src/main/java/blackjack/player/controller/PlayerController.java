package blackjack.player.controller;

import blackjack.exception.GameException;
import blackjack.player.dto.request.UpdatePlayerRequest;
import blackjack.player.dto.response.PlayerResponse;
import blackjack.player.mapper.PlayerMapper;
import blackjack.player.infrastructure.persistence.PlayerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    @PutMapping("/{playerId}")
    public Mono<ResponseEntity<PlayerResponse>> updatePlayer(
            @PathVariable Long playerId,
            @Valid @RequestBody UpdatePlayerRequest request) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new GameException("No se encontró el jugador con ID: "
                + playerId + ".")))
                .flatMap(Player -> {
                    Player.setName(request.newName());
                    return playerRepository.save(Player);
                })
                .map(playerMapper::toResponse)
                .map(ResponseEntity::ok)
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.notFound().build())
                );
    }
}

