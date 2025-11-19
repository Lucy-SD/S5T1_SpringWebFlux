package blackjack.module.game.application.service;

import blackjack.module.game.application.usecase.GetGameHistory;
import blackjack.module.game.domain.entity.Game;
import blackjack.module.game.domain.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GetHistoryService implements GetGameHistory {

    private final GameRepository gameRepository;

    @Override
    public Flux<Game> getGameHistory(Long playerId) {
        return gameRepository.findByPlayerId(playerId);
    }
}
