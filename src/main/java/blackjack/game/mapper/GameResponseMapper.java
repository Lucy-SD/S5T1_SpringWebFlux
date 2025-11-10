package blackjack.game.mapper;

import blackjack.game.dto.response.GameResponse;
import blackjack.game.model.GameState;
import blackjack.game.domain.GameStatus;
import blackjack.score.ScoreCalculator;

public class GameResponseMapper {

    private final ScoreCalculator scoreCalculator;

    public GameResponseMapper(ScoreCalculator scoreCalculator) {
        this.scoreCalculator = scoreCalculator;
    }

    public GameResponse toGameResponse(GameState gameState, String gameId, GameStatus status) {
        return new GameResponse(
                gameId,
                gameState.getPlayer().getName(),
                gameState.getPlayer().getHand(),
                gameState.getPlayer().getScore(),
                gameState.getDealer().getVisibleCards(),
                calculateDealerVisibleScore(gameState),
                gameState.getDealer().getHiddenCard() != null,
                status
        );
    }

    private int calculateDealerVisibleScore(GameState gameState) {
        return scoreCalculator.calculate(gameState.getDealer().getVisibleCards());
    }
}
