package blackjack.aahhrefact.module.game.application.dto.response;

import blackjack.aahhrefact.module.deck.domain.entity.Card;
import blackjack.aahhrefact.module.game.domain.valueObject.GameStatus;

import java.util.List;

public record GameResponse(
        String gameId,
        String playerName,
        List<Card> playerHand,
        int playerScore,
        List<Card> visibleCards,
        int visibleScore,
        boolean hasHiddenCard,
        GameStatus status
) {
}
