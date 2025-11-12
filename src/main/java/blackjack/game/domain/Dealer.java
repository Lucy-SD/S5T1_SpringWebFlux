package blackjack.game.domain;

import blackjack.aahhrefact.module.deck.domain.entity.Card;
import blackjack.aahhrefact.module.game.domain.service.ScoreCalculator;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Dealer {

    private List<Card> hand;
    private int score;
    private boolean firstCardHidden;
    private ScoreCalculator scoreCalculator;

    public Dealer() {
        this.hand = new ArrayList<>();
        this.score = 0;
        this.firstCardHidden = true;
        this.scoreCalculator = new ScoreCalculator();
    }

    public void addCard(Card card, boolean hidden) {
        this.hand.add(card);
        if (hidden) {
            this.firstCardHidden = true;
        }
        updateScore();
    }

    public void revealHiddenCard() {
        this.firstCardHidden = false;
        updateScore();
    }

    public void updateScore() {
        if (firstCardHidden && hand.size() > 1) {
            this.score = hand.getFirst().value();
        } else {
            this.score = scoreCalculator.calculate(this.hand);
        }
    }

    public boolean shouldHit() {
        return this.score < 17;
    }

    public boolean hasBlackjack() {
        return this.hand.size() == 2 && scoreCalculator.calculate(this.hand) == 21;
    }

    public boolean isBust() {
        return this.score > 21;
    }

    public List<Card> getVisibleCards() {
        if (firstCardHidden && this.hand.size() > 1) {
            return List.of(this.hand.getFirst());
        }
        return new ArrayList<>(this.hand);
    }

}
