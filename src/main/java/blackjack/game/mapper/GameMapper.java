package blackjack.game.mapper;

import blackjack.exception.GameException;
import blackjack.game.GameEntity;
import blackjack.game.GameState;
import blackjack.gamer.Dealer;
import blackjack.gamer.Player;
import blackjack.gamer.entity.PlayerEntity;
import blackjack.gamer.repository.PlayerRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GameMapper {

    private final PlayerRepository playerRepository;

    public GameMapper(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Mono<GameState> toGameState(GameEntity gameEntity) {
        return playerRepository.findById(gameEntity.getPlayerId())
                .switchIfEmpty(Mono.error(new GameException("No se encontró el jugador con ID: "
                + gameEntity.getPlayerId())))
                .map(playerEntity -> mapToGameState(gameEntity, playerEntity));
    }

    private GameState mapToGameState(GameEntity gameEntity, PlayerEntity playerEntity) {
        Player player = new Player(playerEntity.getName());
        player.getHand().addAll(gameEntity.getPlayerHand());
        player.updateScore();

        Dealer dealer = new Dealer();
        dealer.getHand().addAll(gameEntity.getDealerHand());
        dealer.updateScore();
        if (!gameEntity.isDealerHasHiddenCard()) {
            dealer.revealHiddenCard();
        }

        return new GameState(dealer, player);
    }

    public GameEntity toGameEntity(GameState gameState, Long playerId) {
        return new GameEntity(gameState, playerId);
    }
}
