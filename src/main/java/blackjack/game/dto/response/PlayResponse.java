package blackjack.game.dto.response;

import blackjack.game.domain.GameResult;
import blackjack.game.domain.GameStatus;

public record PlayResponse(
        GameStatus status,
        GameResponse gameState,
        GameResult finalResult
) {}
