package blackjack.gamer;

import blackjack.deck.Card;
import blackjack.game.score.ScoreCalculator;

import java.util.ArrayList;
import java.util.List;

public class Player implements Gamer {

    private String name;
    private List<Card> hand;
    private int score;
    private ScoreCalculator scoreCalculator;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.score = 0;
        this.scoreCalculator = new ScoreCalculator();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<Card> getHand() {
        return this.hand;
    }

    @Override
    public int getScore() {
        return this.score;
    }

    public void addCard(Card card) {
        this.hand.add(card);
        this.score = scoreCalculator.calculate(this.hand);
    }
}
