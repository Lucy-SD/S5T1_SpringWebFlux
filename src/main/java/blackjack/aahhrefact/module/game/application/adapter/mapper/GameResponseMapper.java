package blackjack.aahhrefact.module.game.application.adapter.mapper;

import blackjack.aahhrefact.module.game.application.dto.response.GameResponse;
import blackjack.aahhrefact.module.game.domain.entity.Game;
import org.springframework.stereotype.Component;

@Component
public class GameResponseMapper {

    public GameResponse toResponse(Game game, String playerName) {
        return new GameResponse(
                game.getId(),
                playerName,
                game.getPlayerHand(),
                game.getPlayerScore(),
                game.getVisibleCards(),
                game.calculateVisibleScore(),
                game.isHasHiddenCard(),
                game.getStatus()
        );
    }
}