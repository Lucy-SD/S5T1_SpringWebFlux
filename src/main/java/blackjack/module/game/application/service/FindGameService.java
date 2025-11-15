package blackjack.module.game.application.service;

import blackjack.module.game.application.usecase.GetGameById;
import blackjack.module.game.domain.entity.Game;
import blackjack.module.game.domain.port.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FindGameService implements GetGameById {

    private final GameRepository gameRepository;

    @Override
    public Mono<Game> getGameById(String id) {
        return gameRepository.findById(id);
    }
}
