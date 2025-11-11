package blackjack.aahhrefact.module.player.application.adapter.mapper;

import blackjack.aahhrefact.module.player.domain.entity.Player;
import blackjack.aahhrefact.module.player.application.dto.response.PlayerResponse;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public PlayerResponse toResponse(Player player) {

        return new PlayerResponse(
                player.getId().toString(),
                player.getName(),
                player.getGamesWon(),
                player.getGamesLost(),
                player.getGamesPushed(),
                player.getTotalGames(),
                player.getWinRate()
        );
    }
}
