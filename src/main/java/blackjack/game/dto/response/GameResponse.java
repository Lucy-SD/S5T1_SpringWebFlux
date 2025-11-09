package blackjack.game.dto.response;

import blackjack.deck.Card;
import blackjack.game.model.GameStatus;

import java.util.List;

public record GameResponse(
        String gameId,
        String playerName,
        List<Card> playerHand,
        int playerScore,
        List<Card> dealerVisibleCards,
        int dealerVisibleScore,
        boolean dealerHasHiddenCard,
        GameStatus status
) {
}
