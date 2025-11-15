package blackjack.aahhrefact.module.game.infrastructure.persistence;

import blackjack.aahhrefact.module.game.domain.entity.Game;
import blackjack.aahhrefact.module.game.domain.port.GameRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
                .playerHand(entity.getPlayerHand())
                .dealerHand(entity.getDealerHand())
                .playerScore(entity.getPlayerScore())
                .dealerScore(entity.getDealerScore())
                .hasHiddenCard(entity.getFirstCardHidden())
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
                .firstCardHidden(game.isHasHiddenCard())
                .status(game.getStatus())
                .result(game.getResult())
                .createdAt(game.getCreatedAt())
                .build();
    }
}
