package blackjack.aahhrefact.module.ranking.application.adapter.mapper;

import blackjack.aahhrefact.module.player.domain.entity.Player;
import blackjack.aahhrefact.module.ranking.application.dto.response.RankingEntry;
import org.springframework.stereotype.Component;

@Component
public class RankingMapper {

    public RankingEntry toRankingEntry(Player player, int position) {
        return new RankingEntry(
                player.getName(),
                position,
                player.calculateTotalGames(),
                player.getGamesWon(),
                player.calculateWinRate()
        );
    }
}
