package blackjack.aahhrefact.module.player.infrastructure.controller;

import blackjack.aahhrefact.module.player.application.adapter.mapper.PlayerMapper;
import blackjack.aahhrefact.module.player.application.dto.response.PlayerResponse;
import blackjack.aahhrefact.module.player.application.usecase.GetPlayer;
import blackjack.aahhrefact.module.player.application.usecase.UpdatePlayerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
public class PlayerController {

    private final GetPlayer getPlayer;
    private final UpdatePlayerStats updatePlayerStats;
    private final PlayerMapper mapper;

    @GetMapping("/{id}")
    public Mono<PlayerResponse> getPlayer(@PathVariable Long id){
        return getPlayer.getPlayer(id)
                .map(mapper::toResponse);
    }

    @PutMapping("/{id}/wins")
    public Mono<PlayerResponse> incrementWins(@PathVariable Long id){
        return updatePlayerStats.incrementWins(id)
                .map(mapper::toResponse);
    }

    @PutMapping("/{id}/losses")
    public Mono<PlayerResponse> incrementLosses(@PathVariable Long id){
        return updatePlayerStats.incrementLosses(id)
                .map(mapper::toResponse);
    }

    @PutMapping("/{id}/pushes")
    public Mono<PlayerResponse> incrementPushes(@PathVariable Long id){
        return updatePlayerStats.incrementPushes(id)
                .map(mapper::toResponse);
    }
}
