package blackjack.game.entity;

import blackjack.deck.Card;
import blackjack.game.model.GameState;
import blackjack.game.model.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
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
        this.playerHand = new ArrayList<>(gameState.getPlayer().getHand());
        this.dealerHand = new ArrayList<>(gameState.getDealer().getHand());
        this.isDealerFirstCardHidden = true;
        this.playerScore = gameState.getPlayer().getScore();
        this.dealerScore = gameState.getDealer().getScore();
        this.createdAt = Instant.now();
        this.status = GameStatus.ACTIVE;
    }

    public void updateFromGameState(GameState newGameState) {
        this.playerHand = new ArrayList<>(newGameState.getPlayer().getHand());
        this.dealerHand = new ArrayList<>(newGameState.getDealer().getHand());
        this.playerScore = newGameState.getPlayer().getScore();
        this.dealerScore = newGameState.getDealer().getScore();
        this.isDealerFirstCardHidden = (newGameState.getDealer().getHiddenCard() != null);
    }

    public void revealDealerCard() {
        this.isDealerFirstCardHidden = false;
    }
}
