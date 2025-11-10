package blackjack.gamer.mapper;

import blackjack.gamer.dto.response.PlayerResponse;
import blackjack.gamer.domain.Player;
import blackjack.gamer.entity.PlayerEntity;

public class PlayerMapper {
    public PlayerEntity toEntity(Player player) {
        return new PlayerEntity(player.getName());
    }

    public Player fromEntity(PlayerEntity playerEntity) {
        return new Player(playerEntity.getName());
    }

    public PlayerResponse toResponse(PlayerEntity playerEntity) {
        int totalGames = playerEntity.getGamesWon() + playerEntity.getGamesLost() + playerEntity.getGamesPushed();
        double winRate = totalGames > 0 ? (double) playerEntity.getGamesWon() / totalGames : 0.0;

        return new PlayerResponse(
                playerEntity.getId().toString(),
                playerEntity.getName(),
                playerEntity.getGamesWon(),
                playerEntity.getGamesLost(),
                playerEntity.getGamesPushed(),
                totalGames,
                winRate
        );
    }
}
