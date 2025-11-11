package blackjack.player.infrastructure.web.mapper;

import blackjack.player.domain.Player;
import blackjack.player.application.dto.response.PlayerResponse;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public PlayerResponse toResponse(Player player) {
        int totalGames = player.getGamesWon() + player.getGamesLost() + player.getGamesPushed();
        double winRate = totalGames > 0 ? (double) player.getGamesWon() / totalGames : 0.0;

        return new PlayerResponse(
                player.getId().toString(),
                player.getName(),
                player.getGamesWon(),
                player.getGamesLost(),
                player.getGamesPushed(),
                totalGames,
                winRate
        );
    }
}
