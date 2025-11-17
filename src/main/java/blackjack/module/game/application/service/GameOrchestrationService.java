package blackjack.module.game.application.service;

import blackjack.module.game.application.usecase.FinishGame;
import blackjack.module.game.application.usecase.SaveGame;
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

    private final SaveGame saver;
    private final StatsUpdateService stats;
    private final FinishGame finisher;

    public Mono<Game> finishGameAndHandleStats(String gameId) {
        return finisher.finish(gameId)
                .flatMap(finishedGame ->
                        stats.updateStatsWhenGameFinished(finishedGame)
                                .thenReturn(finishedGame)
                );

    }
}
