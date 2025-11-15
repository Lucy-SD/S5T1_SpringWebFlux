package blackjack.aahhrefact.module.ranking.application.service;

import blackjack.aahhrefact.module.player.domain.entity.Player;
import blackjack.aahhrefact.module.player.domain.port.PlayerRepository;
import blackjack.aahhrefact.module.ranking.application.adapter.mapper.RankingMapper;
import blackjack.aahhrefact.module.ranking.application.dto.response.RankingEntry;
import blackjack.aahhrefact.module.ranking.application.usecase.GetPlayersRanking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService implements GetPlayersRanking {
    private final PlayerRepository playerRepository;
    private final RankingMapper mapper;


    @Override
    public Mono<List<RankingEntry>> getTopPlayers(int  limit) {
        return playerRepository.findAll()
                .collectList()
                .map(players -> {
                    AtomicInteger position = new AtomicInteger(1);
                    return players.stream()
                            .filter(player -> player.calculateTotalGames() > 0)
                            .sorted(Comparator.comparingDouble(Player::calculateWinRate).reversed())
                            .limit(limit)
                            .map(player -> mapper.toRankingEntry(player, position.getAndIncrement()))
                            .collect(Collectors.toList());
                });
    }
}
