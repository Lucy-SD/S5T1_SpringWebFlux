package blackjack.module.player.application.adapter.mapper;

import blackjack.module.player.domain.entity.Player;
import blackjack.module.player.application.dto.response.PlayerResponse;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public PlayerResponse toResponse(Player player) {

        return new PlayerResponse(
                player.getId(),
                player.getName(),
                player.getGamesWon(),
                player.getGamesLost(),
                player.getGamesPushed(),
                player.calculateTotalGames(),
                player.calculateWinPercentage()
        );
    }
}
