package blackjack.aahhrefact.module.game.application.service;

import blackjack.aahhrefact.module.game.application.usecase.CreateGame;
import blackjack.aahhrefact.module.game.application.usecase.DealInitialCards;
import blackjack.aahhrefact.module.game.domain.entity.Game;
import blackjack.aahhrefact.module.game.domain.port.GameRepository;
import blackjack.aahhrefact.module.game.domain.valueObject.GameStatus;
import blackjack.aahhrefact.module.player.application.usecase.FindOrCreatePlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GameCreatorService implements CreateGame {
    private final GameRepository gameRepository;
    private final FindOrCreatePlayer playerFinder;
    private final DealInitialCards dealInitialCards;

    @Override
    public Mono<Game> create(String playerName) {
        return playerFinder.findOrCreatePlayer(playerName)
                .map(player -> Game.builder()
                        .playerId(player.getId())
                        .status(GameStatus.ACTIVE)
                        .build())
                .flatMap(dealInitialCards::deal)
                .flatMap(gameRepository::save);
    }
}
