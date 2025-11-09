package blackjack.game.dto.response;

import blackjack.game.model.GameResult;
import blackjack.game.model.GameStatus;

public record PlayResponse(
        GameStatus status,
        GameResponse gameState,
        GameResult finalResult
) {}
