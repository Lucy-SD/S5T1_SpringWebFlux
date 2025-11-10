package blackjack.player.model;

import blackjack.deck.Card;
import blackjack.score.ScoreCalculator;

import java.util.ArrayList;
import java.util.List;

public class Dealer{

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

}
