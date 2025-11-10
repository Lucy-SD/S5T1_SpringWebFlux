package blackjack.ranking.controller;

import blackjack.gamer.entity.PlayerEntity;
import blackjack.gamer.repository.PlayerRepository;
import blackjack.ranking.dto.response.RankingEntry;
import blackjack.ranking.dto.response.RankingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/ranking")
@RequiredArgsConstructor
public class RankingController {
    private final PlayerRepository playerRepository;

    @GetMapping
    public Mono<ResponseEntity<RankingResponse>> getRanking() {
        return playerRepository.findAll()
                .filter(player -> (player.getGamesWon() + player.getGamesLost())
                + player.getGamesPushed() > 0)
                .sort(Comparator
                        .comparingDouble(this::calculateWinRate)
                        .thenComparingInt(PlayerEntity::getGamesWon)
                        .reversed()
                )
                .collectList()
                .map(players -> {
                    AtomicInteger position = new AtomicInteger(1);
                    var rankingEntries = players.stream()
                            .map(player -> new RankingEntry(
                                    player.getName(),
                            position.getAndIncrement(),
                            player.getGamesWon() + player.getGamesLost() + player.getGamesPushed(),
                            player.getGamesWon(),
                            calculateWinRate(player)
                            ))
                            .toList();
                    return new RankingResponse(rankingEntries);
                })
                .map(ResponseEntity::ok);
    }

    private double calculateWinRate(PlayerEntity player) {
        int totalGames = player.getGamesWon() + player.getGamesLost() + player.getGamesPushed();
        return totalGames > 0 ? (double) player.getGamesWon() / totalGames : 0.0;
    }
}
