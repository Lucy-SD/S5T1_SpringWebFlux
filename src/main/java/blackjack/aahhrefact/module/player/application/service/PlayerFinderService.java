package blackjack.aahhrefact.module.player.application.service;

import blackjack.aahhrefact.module.player.application.usecase.FindOrCreatePlayer;
import blackjack.aahhrefact.module.player.application.usecase.GetPlayer;
import blackjack.aahhrefact.module.player.application.usecase.UpdatePlayerStats;
import blackjack.aahhrefact.module.player.domain.entity.Player;
import blackjack.aahhrefact.module.player.domain.port.PlayerRepository;
import blackjack.exception.GameException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PlayerFinderService implements FindOrCreatePlayer, GetPlayer, UpdatePlayerStats {
    private final PlayerRepository playerRepository;

    @Override
    public Mono<Player> findPlayerById(Long id) {
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameException(("No se encotró el jugador con ID: " + id + "."))));
    }

    @Override
    public Mono<Player> findOrCreatePlayerByName(String name) {
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
                    player.increaseGamesWon();
                    return playerRepository.save(player);
                });
    }

    @Override
    public Mono<Player> incrementLosses(Long playerId) {
        return playerRepository.findById(playerId)
                .flatMap(player -> {
                    player.increaseGamesLost();
                    return playerRepository.save(player);
                });
    }

    @Override
    public Mono<Player> incrementPushes(Long playerId) {
        return playerRepository.findById(playerId)
                .flatMap(player -> {
                    player.increaseGamesPushed();
                    return playerRepository.save(player);
                });
    }
}
