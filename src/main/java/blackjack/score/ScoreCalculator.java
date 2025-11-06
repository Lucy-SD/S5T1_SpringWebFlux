package blackjack.score;

import blackjack.deck.Card;

import java.util.List;

public class ScoreCalculator {

    public int calculate(List<Card> hand) {
        int score = 0;
        int aceCount = 0;

        for (Card card : hand) {
            if (card.value() == 1) {
                aceCount++;
                score += 11;
            } else {
                score += card.value();
            }
        }

        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }

        return score;
    }
}
