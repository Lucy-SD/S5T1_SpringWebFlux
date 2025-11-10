package blackjack.game.mapper;

import blackjack.exception.GameException;
import blackjack.game.domain.Game;
import blackjack.game.model.GameState;
import blackjack.gamer.model.Dealer;
import blackjack.gamer.domain.Player;
import blackjack.gamer.infrastructure.persistence.PlayerRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GameMapper {

    private final PlayerRepository playerRepository;

    public GameMapper(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Mono<GameState> toGameState(Game Game) {
        return playerRepository.findById(Game.getPlayerId())
                .switchIfEmpty(Mono.error(new GameException("No se encontró el jugador con ID: "
                + Game.getPlayerId() + ".")))
                .map(Player -> mapToGameState(Game, Player));
    }

    private GameState mapToGameState(Game Game, Player Player) {
        Player player = new Player(Player.getName());
        player.getHand().addAll(Game.getPlayerHand());
        player.updateScore();

        Dealer dealer = new Dealer();
        dealer.getHand().addAll(Game.getDealerHand());
        dealer.updateScore();
        if (!Game.isDealerFirstCardHidden()) {
            dealer.revealHiddenCard();
        }

        return new GameState(dealer, player);
    }

    public Game toGame(GameState gameState, Long playerId) {
        return new Game(gameState, playerId);
    }
}
