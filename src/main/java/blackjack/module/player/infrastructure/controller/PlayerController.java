package blackjack.module.player.infrastructure.controller;

import blackjack.module.player.application.adapter.mapper.PlayerMapper;
import blackjack.module.player.application.dto.response.PlayerResponse;
import blackjack.module.player.application.usecase.GetPlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
public class PlayerController {

    private final GetPlayer getPlayer;
    private final PlayerMapper mapper;

    @GetMapping("/{id}")
    public Mono<PlayerResponse> getPlayerById(@PathVariable String playerId) {
        Long id = Long.parseLong(playerId);
        return getPlayer.getPlayerById(id)
                .map(mapper::toResponse);
    }

    @GetMapping("/name/{name}")
    public Mono<PlayerResponse> getPlayerByName(@PathVariable String name){
        return getPlayer.getPlayerByName(name)
                .map(mapper::toResponse);
    }
}
