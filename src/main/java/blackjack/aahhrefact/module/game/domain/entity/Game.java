package blackjack.aahhrefact.module.game.domain.entity;

import blackjack.aahhrefact.module.deck.domain.entity.Card;
import blackjack.aahhrefact.module.game.domain.valueObject.GameResult;
import blackjack.aahhrefact.module.game.domain.valueObject.GameStatus;
import blackjack.aahhrefact.module.game.domain.valueObject.Winner;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Game {

    private String id;
    private Long playerId;

    @Builder.Default
    private List<Card> playerHand = new ArrayList<>();

    @Builder.Default
    private List<Card> dealerHand = new ArrayList<>();

    @Builder.Default
    private boolean firstCardHidden = true;

    private int playerScore;
    private int dealerScore;

    @Builder.Default
    private GameStatus status = GameStatus.ACTIVE;

    private GameResult result;

    @Builder.Default
    private Instant createdAt = Instant.now();

    public int scoreCalculator(List<Card> hand) {
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

    public List<Card> getVisibleCards() {
        if (this.firstCardHidden && !this.dealerHand.isEmpty()) {
            return this.dealerHand.subList(1, this.dealerHand.size());
        }
        return this.dealerHand;
    }

    public int calculateVisibleScore() {
        if (this.firstCardHidden) {
            return this.dealerHand.getFirst().value();
        }
        return this.scoreCalculator(this.dealerHand);
    }

    public boolean canPlayerHit() {
        return this.status == GameStatus.ACTIVE && this.playerScore < 21;
    }

    public Winner determineWinner() {
        if (this.playerScore > 21) return Winner.DEALER;
        if (this.dealerScore > 21) return Winner.PLAYER;
        if (this.playerScore > this.dealerScore) return Winner.PLAYER;
        if (this.dealerScore > this.playerScore) return Winner.DEALER;
        return Winner.PUSH;
    }
}
