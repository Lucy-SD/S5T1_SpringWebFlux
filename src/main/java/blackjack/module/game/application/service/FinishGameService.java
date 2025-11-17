package blackjack.module.game.application.service;

import blackjack.module.game.application.usecase.FinishGame;
import blackjack.module.game.domain.entity.Game;
import blackjack.module.game.domain.port.GameRepository;
import blackjack.module.game.domain.valueObject.GameResult;
import blackjack.module.game.domain.valueObject.GameStatus;
import blackjack.shared.exception.GameException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class FinishGameService implements FinishGame {
    private final GameRepository gameRepository;

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
    public Mono<Game> finish(Game game) {
        return gameRepository.findById(game.getId())
                        .switchIfEmpty(Mono.error(new GameException("No se encontro el juego")))
                .map(foundGame -> {
                    foundGame.setStatus(GameStatus.FINISHED);
                    foundGame.setResult(new GameResult(
                            foundGame.determineWinner(),
                            foundGame.getDealerScore(),
                            foundGame.getPlayerScore()
                    ));
                    return foundGame;
                })
                .flatMap(gameRepository::save)
                .doOnError(error -> log.error("Error finalizando el juego: {}", error.getMessage()));
    }
}
