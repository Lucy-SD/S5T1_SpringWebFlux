package blackjack.aahhrefact.module.game.domain.entity;

import blackjack.aahhrefact.module.deck.domain.entity.Card;
import blackjack.aahhrefact.module.game.domain.valueObject.GameResult;
import blackjack.aahhrefact.module.game.domain.valueObject.GameStatus;
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

    public List<Card> getVisibleCards() {
        if (this.firstCardHidden && !this.dealerHand.isEmpty()) {
            return this.dealerHand.subList(1, this.dealerHand.size());
        }
        return this.dealerHand;
    }

    public int getVisibleScore() {
        if (this.firstCardHidden && this.dealerHand.size() > 1) {
            int visibleScore = 0;
            for (int i = 1; i < this.dealerHand.size(); i++) {
                visibleScore += dealerHand.get(i).value();
            }
            return visibleScore;
        }
        return this.dealerScore;
    }
 }
