package blackjack.gamer.mapper;

import blackjack.gamer.dto.response.PlayerResponse;
import blackjack.gamer.domain.Player;

public class PlayerMapper {
    public Player toEntity(Player player) {
        return new Player(player.getName());
    }

    public Player fromEntity(Player Player) {
        return new Player(Player.getName());
    }

    public PlayerResponse toResponse(Player Player) {
        int totalGames = Player.getGamesWon() + Player.getGamesLost() + Player.getGamesPushed();
        double winRate = totalGames > 0 ? (double) Player.getGamesWon() / totalGames : 0.0;

        return new PlayerResponse(
                Player.getId().toString(),
                Player.getName(),
                Player.getGamesWon(),
                Player.getGamesLost(),
                Player.getGamesPushed(),
                totalGames,
                winRate
        );
    }
}
