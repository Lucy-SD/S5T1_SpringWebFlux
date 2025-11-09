package blackjack.game;

import blackjack.deck.Card;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "game")
public class GameEntity {

    @Id
    private String id;

    private Long playerId;

    private List<Card> playerHand;
    private List<Card> dealerHand;
    private boolean isDealerFirstCardHidden;
    private int playerScore;
    private int dealerScore;

    private GameStatus status;
    private Instant createdAt;

    public GameEntity() {}

    public GameEntity(GameState gameState, Long playerId) {
        this.playerId = playerId;
        this.playerHand = gameState.getPlayer().getHand();
        this.dealerHand = gameState.getDealer().getHand();
        this.isDealerFirstCardHidden = true;
        this.playerScore = gameState.getPlayer().getScore();
        this.dealerScore = gameState.getDealer().getScore();
        this.createdAt = Instant.now();
        this.status = GameStatus.ACTIVE;
    }
}
