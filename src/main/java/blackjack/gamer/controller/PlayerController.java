package blackjack.gamer.controller;

import blackjack.exception.GameException;
import blackjack.gamer.dto.request.UpdatePlayerRequest;
import blackjack.gamer.dto.response.PlayerResponse;
import blackjack.gamer.mapper.PlayerMapper;
import blackjack.gamer.repository.PlayerRepository;
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
                .flatMap(playerEntity -> {
                    playerEntity.setName(request.newName());
                    return playerRepository.save(playerEntity);
                })
                .map(playerMapper::toResponse)
                .map(ResponseEntity::ok)
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.notFound().build())
                );
    }
}

