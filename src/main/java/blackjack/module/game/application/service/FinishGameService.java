package blackjack.module.game.application.service;

import blackjack.module.game.application.usecase.FinishGame;
import blackjack.module.game.domain.entity.Game;
import blackjack.module.game.domain.port.GameRepository;
import blackjack.module.game.domain.valueObject.GameResult;
import blackjack.module.game.domain.valueObject.GameStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
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
        game.setStatus(GameStatus.FINISHED);
        game.setResult(new GameResult(
                game.determineWinner(),
                game.getDealerScore(),
                game.getPlayerScore()
        ));
        return gameRepository.save(game);
    }
}
