package blackjack.module.player.infrastructure.controller;

import blackjack.module.player.application.adapter.mapper.PlayerMapper;
import blackjack.module.player.application.dto.response.PlayerResponse;
import blackjack.module.player.application.usecase.FindOrCreatePlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
public class PlayerController {

    private final FindOrCreatePlayer getPlayer;
    private final PlayerMapper mapper;

    @GetMapping("/{id}")
    public Mono<PlayerResponse> getPlayerById(@PathVariable Long id) {
     //   Long id = Long.parseLong(playerId);
        return getPlayer.findPlayerById(id)
                .map(mapper::toResponse);
    }

    @GetMapping("/name/{name}")
    public Mono<PlayerResponse> getPlayerByName(@PathVariable String name){
        return getPlayer.findOrCreatePlayerByName(name)
                .map(mapper::toResponse);
    }

    @PutMapping("/{id}")
    public Mono<PlayerResponse> updatePlayersName(@PathVariable Long id) {
        return getPlayer.findPlayerById(id)
                .
    }
}
