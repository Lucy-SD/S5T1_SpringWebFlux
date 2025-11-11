package blackjack.game.application.service;

import blackjack.game.application.usecase.FinishGame;
import blackjack.game.domain.Game;
import blackjack.game.infrastructure.persistence.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FinishGameService implements FinishGame {
    private final GameRepository gameRepository;

    @Override
    public Mono<Game> shouldFinish(Game game) { //TODO
        return Mono.just(game);
    }

    @Override
    public Mono<Game> finish(String gameId) { //TODO
        return Mono.empty();
    }
}
