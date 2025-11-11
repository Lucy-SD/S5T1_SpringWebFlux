package blackjack.aahhrefact.module.game.application.dto.response;

import blackjack.aahhrefact.module.game.domain.valueObject.GameResult;
import blackjack.aahhrefact.module.game.domain.valueObject.GameStatus;

public record PlayResponse(
        GameStatus status,
        GameResponse gameState,
        GameResult finalResult
) {}
