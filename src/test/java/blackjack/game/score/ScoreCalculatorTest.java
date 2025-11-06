package blackjack.game.score;

import blackjack.deck.Card;
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

    @Test
    void calculateScore_withAceAndScoreUnder21_shouldValueAceAs11() {
        List<Card> hand = List.of(new Card(7), new Card(1));
        ScoreCalculator calculator = new ScoreCalculator();
        assertThat(calculator.calculate(hand)).isEqualTo(18);
    }

    @Test
    void calculateScore_withAceAndScoreOver21_shouldValueAceAs1() {
        List<Card> hand = List.of(new Card(7), new Card(1), new Card(8), new Card(5));
        ScoreCalculator calculator = new ScoreCalculator();
        assertThat(calculator.calculate(hand)).isEqualTo(21);
    }

    @Test
    void calculateScore_withMoreThanOneAce_shouldValueOneAceAs11AndOthersAs1() {
        List<Card> hand = List.of(new Card(1), new Card(1), new Card(7));
        ScoreCalculator calculator = new ScoreCalculator();
        assertThat(calculator.calculate(hand)).isEqualTo(19);
    }
}
