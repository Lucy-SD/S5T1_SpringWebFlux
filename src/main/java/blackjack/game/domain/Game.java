package blackjack.game.domain;

import blackjack.aahhrefact.module.deck.domain.entity.Card;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document(collection = "game")
public class Game {

    @Id
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

    public void revealDealerCard() {
        this.isDealerFirstCardHidden = false;
    }
}
