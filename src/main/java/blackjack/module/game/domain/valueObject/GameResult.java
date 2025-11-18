package blackjack.module.game.domain.valueObject;

public record GameResult(Winner winner, int playerScore,  int dealerScore) {
}
