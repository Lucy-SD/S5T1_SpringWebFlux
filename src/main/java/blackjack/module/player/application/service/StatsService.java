package blackjack.module.player.application.service;

import blackjack.module.player.application.usecase.UpdatePlayerStats;
import blackjack.module.player.domain.entity.Player;
import blackjack.module.player.domain.port.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StatsService implements UpdatePlayerStats {
    private final PlayerRepository playerRepository;

    @Override
    @Transactional
    public Mono<Player> incrementWins(Long playerId) {
        return playerRepository.findById(playerId)
                .flatMap(player -> {
                    player.increaseGamesWon();
                    return playerRepository.save(player);
                });
    }

    @Override
    @Transactional
    public Mono<Player> incrementLosses(Long playerId) {
        return playerRepository.findById(playerId)
                .flatMap(player -> {
                    player.increaseGamesLost();
                    return playerRepository.save(player);
                });
    }

    @Override
    @Transactional
    public Mono<Player> incrementPushes(Long playerId) {
        return playerRepository.findById(playerId)
                .flatMap(player -> {
                    player.increaseGamesPushed();
                    return playerRepository.save(player);
                });
    }
}
