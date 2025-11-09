package blackjack.gamer.mapper;

import blackjack.gamer.Player;
import blackjack.gamer.entity.PlayerEntity;

public class PlayerMapper {
    public PlayerEntity toEntity(Player player) {
        return new PlayerEntity(player.getName());
    }

    public Player fromEntity(PlayerEntity playerEntity) {
        return new Player(playerEntity.getName());
    }
}
