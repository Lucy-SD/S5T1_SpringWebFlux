package blackjack.aahhrefact.module.player.application.service;

import blackjack.aahhrefact.module.player.application.usecase.FindOrCreatePlayer;
import blackjack.aahhrefact.module.player.application.usecase.GetPlayer;
import blackjack.aahhrefact.module.player.application.usecase.UpdatePlayerStats;
import blackjack.aahhrefact.module.player.domain.entity.Player;
import blackjack.aahhrefact.module.player.domain.port.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PlayerFinderService implements FindOrCreatePlayer, GetPlayer, UpdatePlayerStats {
    private final PlayerRepository playerRepository;

    @Override
    public Mono<Player> findOrCreatePlayer(String name) {
        return playerRepository.existsByName(name)
                .flatMap(exists -> exists
                ? playerRepository.findByName(name)
                        : createNewPlayer(name));
    }

    @Override
    public Mono<Player> getPlayer(Long id) {
        return playerRepository.findById(id);
    }

    private Mono<Player> createNewPlayer(String name) {
        Player newPlayer = Player.builder()
                .name(name)
                .gamesWon(0)
                .gamesLost(0)
                .gamesPushed(0)
                .build();
        return playerRepository.save(newPlayer);
    }

    @Override
    public Mono<Player> incrementWins(Long playerId) {
        return playerRepository.findById(playerId)
                .flatMap(player -> {
                    player.setGamesWon(player.getGamesWon() + 1);
                    return playerRepository.save(player);
                });
    }

    @Override
    public Mono<Player> incrementLosses(Long playerId) {
        return playerRepository.findById(playerId)
                .flatMap(player -> {
                    player.setGamesLost(player.getGamesLost() + 1);
                    return playerRepository.save(player);
                });
    }

    @Override
    public Mono<Player> incrementPushes(Long playerId) {
        return playerRepository.findById(playerId)
                .flatMap(player -> {
                    player.setGamesPushed(player.getGamesPushed() + 1);
                    return playerRepository.save(player);
                });
    }
}
