package blackjack.module.game.application.dto.response;

import blackjack.module.game.domain.valueObject.GameResult;
import blackjack.module.game.domain.valueObject.GameStatus;

public record PlayResponse(
        GameStatus status,
        GameResponse gameState,
        GameResult result
) {}
