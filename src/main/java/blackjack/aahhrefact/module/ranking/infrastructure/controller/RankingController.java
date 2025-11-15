package blackjack.aahhrefact.module.ranking.infrastructure.controller;

import blackjack.aahhrefact.module.player.domain.entity.Player;
import blackjack.aahhrefact.module.ranking.application.dto.response.RankingResponse;
import blackjack.aahhrefact.module.ranking.application.usecase.GetPlayersRanking;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/ranking")
@RequiredArgsConstructor
public class RankingController {
    private final GetPlayersRanking getPlayersRanking;

    @GetMapping
    public Mono<ResponseEntity<RankingResponse>> getRanking(
            @RequestParam(defaultValue = "10") int limit) {
        return getPlayersRanking.getTopPlayers(limit)
                .map(RankingResponse::new)
                .map(ResponseEntity::ok);
    }
}
