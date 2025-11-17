package blackjack.module.game.application.dto.response;

import blackjack.module.deck.application.dto.response.CardResponse;
import blackjack.module.game.domain.valueObject.GameStatus;

import java.util.List;

public record GameResponse(
        String gameId,
        String playerName,
        List<CardResponse> playerHand,
        int playerScore,
        List<CardResponse> visibleCards,
        int visibleScore,
        boolean hasHiddenCard,
        GameStatus status
) {
}
