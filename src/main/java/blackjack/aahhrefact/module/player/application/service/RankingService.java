package blackjack.aahhrefact.module.player.application.service;

import blackjack.aahhrefact.module.player.application.usecase.GetRanking;
import blackjack.aahhrefact.module.player.domain.entity.Player;
import blackjack.aahhrefact.module.player.domain.port.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class RankingService implements GetRanking {

    private final PlayerRepository playerRepository;

    @Override
    public Flux<Player> getRanking() {
        return playerRepository.findAll()
                .filter(player -> player.calculateTotalGames() > 0)
                .sort(Comparator.comparingDouble(Player::calculateWinRate)
                        .thenComparingInt(Player::getGamesWon)
                        .reversed());
    }
}
