package blackjack.module.game.domain.valueObject;

public record GameResult(Winner winner, int dealerScore, int playerScore) {
}
