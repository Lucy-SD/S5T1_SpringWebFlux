package blackjack.game.application.service;

import blackjack.game.application.usecase.DealersTurn;
import blackjack.game.domain.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DealerTurnService implements DealersTurn {

    @Override
    public Mono<Game> play(Game game) { //TODO
        return Mono.just(game);
    }
}
