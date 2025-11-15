package blackjack.module.ranking.application.usecase;

import blackjack.module.ranking.application.dto.response.RankingEntry;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GetPlayersRanking {
    Mono<List<RankingEntry>> getTopPlayers(int limit);
}
