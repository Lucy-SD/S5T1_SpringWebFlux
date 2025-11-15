package blackjack.aahhrefact.module.player.infrastructure.controller;

import blackjack.aahhrefact.module.game.application.dto.response.GameResponse;
import blackjack.aahhrefact.module.player.application.adapter.mapper.PlayerMapper;
import blackjack.aahhrefact.module.player.application.dto.response.PlayerResponse;
import blackjack.aahhrefact.module.player.application.service.PlayerStatsService;
import blackjack.aahhrefact.module.player.application.usecase.GetPlayer;
import blackjack.aahhrefact.module.player.application.usecase.UpdatePlayerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
public class PlayerController {

    private final GetPlayer getPlayer;
    private final PlayerStatsService updatePlayerStats;
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
