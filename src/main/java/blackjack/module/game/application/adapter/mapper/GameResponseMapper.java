package blackjack.module.game.application.adapter.mapper;

import blackjack.module.deck.application.adapter.mapper.CardMapper;
import blackjack.module.game.application.dto.response.GameResponse;
import blackjack.module.game.application.dto.response.PlayResponse;
import blackjack.module.game.domain.entity.Game;
import blackjack.module.game.domain.valueObject.GameStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameResponseMapper {

    private final CardMapper cardMapper;

    public GameResponse toResponse(Game game, String playerName) {
        return new GameResponse(
                game.getId(),
                playerName,
                game.getPlayerHand().stream()
                        .map(cardMapper::toResponse)
                        .toList(),
                game.getPlayerScore(),
                game.getVisibleCards().stream()
                        .map(cardMapper::toResponse)
                        .toList(),
                game.calculateVisibleScore(),
                game.isHasHiddenCard(),
                game.getStatus()
        );
    }

    public PlayResponse toPlayResponse(Game game, String playerName) {
        GameResponse gameResponse = toResponse(game, playerName);
        if (game.getStatus() == GameStatus.FINISHED && game.getResult() != null) {
            return new PlayResponse(
                    GameStatus.FINISHED,
                    gameResponse,
                    game.getResult()
            );
        }
        return new PlayResponse(
                game.getStatus(),
                gameResponse,
                null
        );
    }
}
