package blackjack.aahhrefact.module.game.application.service;

import blackjack.aahhrefact.module.game.application.usecase.FinishGame;
import blackjack.aahhrefact.module.game.domain.entity.Game;
import blackjack.aahhrefact.module.game.domain.port.GameRepository;
import blackjack.aahhrefact.module.game.domain.valueObject.GameResult;
import blackjack.aahhrefact.module.game.domain.valueObject.GameStatus;
import blackjack.aahhrefact.module.player.application.usecase.UpdatePlayerStats;
import blackjack.aahhrefact.module.player.domain.entity.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FinishGameService implements FinishGame {
    private final GameRepository gameRepository;
    private final UpdatePlayerStats updatePlayerStats;

    @Override
    public Mono<Boolean> shouldFinish(Game game) {
        if (game.getPlayerScore() >= 21) {
            return Mono.just(true);
        }
        if (game.getDealerHand().size() == 2 && game.calculateVisibleScore() == 21) {
            return Mono.just(true);
        }
        return Mono.just(false);
    }

    @Override
    public Mono<Game> finish(String gameId) {
        return gameRepository.findById(gameId)
                .map(game -> {
                    game.setStatus(GameStatus.FINISHED);
                    game.setResult(new GameResult(
                            game.determineWinner(),
                            game.getDealerScore(),
                            game.getPlayerScore()
                    ));
                    return game;
                })
                .flatMap(game -> {
                    return updatePlayerStatsOnResult(game)
                            .thenReturn(game);
                })
                .flatMap(gameRepository::save);
    }

    private Mono<Void> updatePlayerStatsOnResult(Game game) {
        Mono<Player> statsUpdate = switch (game.getResult().winner()) {
            case PLAYER -> updatePlayerStats.incrementWins(game.getPlayerId());
            case DEALER -> updatePlayerStats.incrementLosses(game.getPlayerId());
            case PUSH -> updatePlayerStats.incrementPushes(game.getPlayerId());
        };
        return statsUpdate.then();
    }

}
