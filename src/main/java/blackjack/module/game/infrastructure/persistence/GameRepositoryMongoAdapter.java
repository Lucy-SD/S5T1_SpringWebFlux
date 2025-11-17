package blackjack.module.game.infrastructure.persistence;

import blackjack.module.deck.domain.entity.Deck;
import blackjack.module.game.domain.entity.Game;
import blackjack.module.game.domain.port.GameRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Repository
public class GameRepositoryMongoAdapter implements GameRepository {

    private final GameRepositoryMongo mongo;

    public GameRepositoryMongoAdapter(GameRepositoryMongo mongo) {
        this.mongo = mongo;
    }

    @Override
    public Mono<Game> findById(String id) {
        return mongo.findById(id)
                .map(this::mapToDomain);
    }

    @Override
    public Mono<Game> save(Game game) {
        GameMongoEntity entity = mapToMongo(game);
        return mongo.save(entity)
                .map(this::mapToDomain);
    }

    @Override
    public Flux<Game> findByPlayerId(Long playerId) {
        return mongo.findByPlayerId(playerId)
                .map(this::mapToDomain);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return mongo.deleteById(id).then();
    }

    private Game mapToDomain(GameMongoEntity entity) {
        return Game.builder()
                .id(entity.getId())
                .playerId(entity.getPlayerId())
                .playerHand(entity.getPlayerHand() != null
                        ? entity.getPlayerHand() : new ArrayList<>())
                .dealerHand(entity.getDealerHand() != null
                ? entity.getDealerHand() : new ArrayList<>())
                .playerScore(entity.getPlayerScore())
                .dealerScore(entity.getDealerScore())
                .hasHiddenCard(entity.getHasHiddenCard())
                .deck(new Deck(entity.getRemainingCards() != null
                ? entity.getRemainingCards() : new ArrayList<>()))
                .status(entity.getStatus())
                .result(entity.getResult())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private GameMongoEntity mapToMongo(Game game) {
        return GameMongoEntity.builder()
                .id(game.getId())
                .playerId(game.getPlayerId())
                .playerHand(game.getPlayerHand())
                .dealerHand(game.getDealerHand())
                .playerScore(game.getPlayerScore())
                .dealerScore(game.getDealerScore())
                .hasHiddenCard(game.isHasHiddenCard())
                .remainingCards(game.getDeck().getCards())
                .status(game.getStatus())
                .result(game.getResult())
                .createdAt(game.getCreatedAt())
                .build();
    }
}
