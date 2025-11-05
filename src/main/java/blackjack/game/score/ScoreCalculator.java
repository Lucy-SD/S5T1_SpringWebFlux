package blackjack.game.score;

import blackjack.deck.Card;

import java.util.List;

public class ScoreCalculator {

    public int calculate(List<Card> hand) {
        return hand.stream()
                .mapToInt(Card::value)
                .sum();
    }
}
