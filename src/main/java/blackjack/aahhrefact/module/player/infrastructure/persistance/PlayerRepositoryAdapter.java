package blackjack.aahhrefact.module.player.infrastructure.persistance;

import blackjack.aahhrefact.module.player.domain.entity.Player;
import blackjack.aahhrefact.module.player.domain.port.PlayerRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class PlayerRepositoryAdapter implements PlayerRepository {

    private final PlayerRepositoryMySQL mysql;

    public PlayerRepositoryAdapter(PlayerRepositoryMySQL mysql) {
        this.mysql = mysql;
    }

    @Override
    public Mono<Player> findByName(String name) {
        return mysql.findByNameEntity(name)
                .map(this::mapToDomain);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return mysql.existsByNameEntity(name);
    }

    @Override
    public Mono<Player> findById(Long id) {
        return mysql.findById(id)
                .map(this::mapToDomain);
    }

    @Override
    public Mono<Player> save(Player player) {
        PlayerJpaEntity entity = mapToJpa(player);
        return mysql.save(entity)
                .map(this::mapToDomain);
    }

    @Override
    public Mono<Void> delete(Player player) {
        return mysql.delete(mapToJpa(player));
    }

    private Player mapToDomain(PlayerJpaEntity entity) {
        return Player.builder()
                .id(entity.getId())
                .name(entity.getName())
                .gamesWon(entity.getGamesWon())
                .gamesLost(entity.getGamesLost())
                .gamesPushed(entity.getGamesPushed())
                .build();
    }

    private PlayerJpaEntity mapToJpa(Player player) {
        return PlayerJpaEntity.builder()
                .id(player.getId())
                .name(player.getName())
                .gamesWon(player.getGamesWon())
                .gamesLost(player.getGamesLost())
                .gamesPushed(player.getGamesPushed())
                .build();
    }
}
