package blackjack.game.infrastructure.web.mapper;

import blackjack.game.domain.Game;
import blackjack.game.dto.response.GameResponse;
import blackjack.game.model.GameState;
import blackjack.game.domain.GameStatus;
import blackjack.shared.ScoreCalculator;
import org.springframework.stereotype.Component;

@Component
public class GameResponseMapper {

    private final ScoreCalculator scoreCalculator;

    public GameResponseMapper(ScoreCalculator scoreCalculator) {
        this.scoreCalculator = scoreCalculator;
    }

    public GameResponse toGameResponse(Game game) {
        var dealerVisibleCards = game.isDealerFirstCardHidden() && !game.getDealerHand().isEmpty()
                ? game.getDealerHand().subList(0, 1)
                : game.getDealerHand();

        int dealerVisibleScore = game.isDealerFirstCardHidden() && game.getDealerHand().size() > 1
                ? game.getDealerHand().get(0).value()
                : scoreCalculator.calculate(game.getDealerHand());

        return new GameResponse(
                game.getId(),
                "Player", // Esto debería venir de un PlayerService
                game.getPlayerHand(),
                game.getPlayerScore(),
                dealerVisibleCards,
                dealerVisibleScore,
                game.isDealerFirstCardHidden(),
                game.getStatus()
        );
    }
}