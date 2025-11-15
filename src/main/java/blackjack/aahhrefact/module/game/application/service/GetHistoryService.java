package blackjack.aahhrefact.module.game.application.service;

import blackjack.aahhrefact.module.game.application.usecase.GetGameHistory;
import blackjack.aahhrefact.module.game.domain.entity.Game;
import blackjack.aahhrefact.module.game.domain.port.GameRepository;
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
