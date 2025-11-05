package blackjack.gamer;

import blackjack.deck.Card;
import blackjack.game.score.ScoreCalculator;

import java.util.ArrayList;
import java.util.List;

public class Dealer implements Gamer {

    private List<Card> hand;
    private int score;
    private ScoreCalculator scoreCalculator;

    public Dealer() {
        hand = new ArrayList<>();
        score = 0;
        scoreCalculator = new ScoreCalculator();
    }

    @Override
    public List<Card> getHand() {
        return this.hand;
    }

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public void addCard(Card card) {
        this.hand.add(card);
        this.score = scoreCalculator.calculate(this.hand);
    }
}
