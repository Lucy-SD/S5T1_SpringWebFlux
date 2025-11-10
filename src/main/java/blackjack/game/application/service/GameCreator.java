package blackjack.game.application.service;

import blackjack.game.application.usercase.CreateGame;
import blackjack.game.application.usercase.DealInitialCards;
import blackjack.game.domain.Game;
import blackjack.game.infrastructure.persistence.GameRepository;
import blackjack.player.application.service.PlayerFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class GameCreator implements CreateGame {
    private final PlayerFinder playerFinder;
    private final GameRepository gameRepository;
    private final DealInitialCards dealInitialCards;

    @Override
    public Mono<Game> create(String playerName) {
        return playerFinder.findOrCreate(playerName)
                .flatMap(player -> {
                    Game newGame = Game.builder()
                            .playerId(player.getId())
                            .playerHand(new ArrayList<>())
                            .dealerHand(new ArrayList<>())
                            .build();
                    return gameRepository.save(newGame)
                            .flatMap(dealInitialCards::deal);
                });
    }
}
