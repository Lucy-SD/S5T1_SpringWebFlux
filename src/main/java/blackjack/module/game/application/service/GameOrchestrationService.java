package blackjack.module.game.application.service;

import blackjack.module.game.application.usecase.FinishGame;
import blackjack.module.game.domain.entity.Game;
import blackjack.module.player.application.service.StatsUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameOrchestrationService {

    private final StatsUpdateService stats;
    private final FinishGame finisher;

    public Mono<Game> finishGameAndHandleStats(Game game) {
        return finisher.finish(game)
                .flatMap(finishedGame ->
                        stats.updateStatsWhenGameFinished(finishedGame)
                                .thenReturn(finishedGame)
                );

    }
}
