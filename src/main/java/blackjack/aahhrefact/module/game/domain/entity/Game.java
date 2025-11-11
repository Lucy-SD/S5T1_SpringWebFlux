package blackjack.aahhrefact.module.game.domain.entity;

import blackjack.aahhrefact.module.deck.domain.entity.Card;
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
    private boolean isDealerFirstCardHidden = true;

    private int playerScore;
    private int dealerScore;

    @Builder.Default
    private GameStatus status = GameStatus.ACTIVE;

    @Builder.Default
    private Instant createdAt = Instant.now();

    public List<Card> getDealerVisibleCards() {
        if (this.isDealerFirstCardHidden && !dealerHand.isEmpty()) {
            return this.dealerHand.subList(0, 1);
        }
        return this.dealerHand;
    }

    public int getDealerVisibleScore() {
        if (this.isDealerFirstCardHidden && this.dealerHand.size() > 1) {
            return this.dealerHand.getFirst().value();
        }
        return this.dealerScore;
    }
 }
