package blackjack.module.game.application.service;

import blackjack.module.deck.application.usecase.CreateDeck;
import blackjack.module.game.application.usecase.CreateGame;
import blackjack.module.game.application.usecase.DealInitialCards;
import blackjack.module.game.domain.entity.Game;
import blackjack.module.game.domain.GameRepository;
import blackjack.module.player.application.usecase.FindOrCreatePlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateGameService implements CreateGame {
    private final GameRepository gameRepository;
    private final FindOrCreatePlayer playerFinder;
    private final CreateDeck createDeck;
    private final DealInitialCards dealInitialCards;

    @Override
    public Mono<Game> create(String playerName) {
        return playerFinder.findOrCreatePlayerByName(playerName)
                .flatMap(player -> {
                    Game newGame = Game.newGame(player.getId(), createDeck.createDeck());
                    return dealInitialCards.deal(newGame);
                })
                .flatMap(gameRepository::save);
    }
}
