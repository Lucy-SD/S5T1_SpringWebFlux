package blackjack.game.domain;

public record GameResult(Winner winner, int dealerScore, int playerScore) {
}
