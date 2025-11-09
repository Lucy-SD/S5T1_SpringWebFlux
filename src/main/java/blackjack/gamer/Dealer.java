package blackjack.gamer;

import blackjack.deck.Card;
import blackjack.score.ScoreCalculator;

import java.util.ArrayList;
import java.util.List;

public class Dealer implements Gamer {

    private List<Card> hand;
    private List<Boolean> cardVisibility;
    private int score;
    private ScoreCalculator scoreCalculator;

    public Dealer() {
        this.hand = new ArrayList<>();
        this.cardVisibility = new ArrayList<>();
        this.score = 0;
        this.scoreCalculator = new ScoreCalculator();
    }

    @Override
    public List<Card> getHand() {
        return this.hand;
    }

    @Override
    public int getScore() {
        return this.score;
    }

    public void updateScore() {
        this.score = scoreCalculator.calculate(this.hand);
    }

    @Override
    public void addCard(Card card) {
        this.addCard(card, true);
    }

    public void addCard(Card card, boolean visible) {
        this.hand.add(card);
        this.cardVisibility.add(visible);
        this.updateScore();
    }

    public List<Card> getVisibleCards() {
        List<Card> visible = new ArrayList<>();
        for (int i = 0; i < this.hand.size(); i++) {
            if (this.cardVisibility.get(i)) {
                visible.add(this.hand.get(i));
            }
        }
        return visible;
    }

    public Card getHiddenCard() {
        for (int i = 0; i < this.hand.size(); i++) {
            if (!this.cardVisibility.get(i)) {
                return this.hand.get(i);
            }
        }
        return null;
    }

    public void revealHiddenCard() {
        this.cardVisibility.set(0, true);
        this.updateScore();
    }

    @Override
    public boolean hasBlackjack() {
        return (this.hand.size() == 2 && this.score == 21);
    }
}
