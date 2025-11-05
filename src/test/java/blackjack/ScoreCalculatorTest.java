package blackjack;

import blackjack.deck.Card;
import blackjack.game.score.ScoreCalculator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ScoreCalculatorTest {

    @Test
    void calculateScore_shouldSumCardValues() {
        List<Card> hand = List.of(new Card(7), new Card(8));
        ScoreCalculator calculator = new ScoreCalculator();
        assertThat(calculator.calculate(hand)).isEqualTo(15);
    }
}
