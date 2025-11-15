package blackjack.module.game.application.service;

import blackjack.module.game.application.usecase.DeleteGame;
import blackjack.module.game.domain.port.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeleteGameService implements DeleteGame {
    private final GameRepository gameRepository;

    @Override
    public Mono<Void> delete(String id) {
        return gameRepository.deleteById(id);
    }
}
