package blackjack.aahhrefact.module.game.domain.valueObject;

import blackjack.game.domain.Winner;

public record GameResult(Winner winner, int dealerScore, int playerScore) {
}
