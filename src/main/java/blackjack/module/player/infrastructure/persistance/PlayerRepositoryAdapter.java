package blackjack.module.player.infrastructure.persistance;

import blackjack.module.player.domain.entity.Player;
import blackjack.module.player.domain.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PlayerRepositoryAdapter implements PlayerRepository {

    private final PlayerRepositoryMySQL mysql;

    @Override
    public Mono<Player> findByName(String name) {
        return mysql.findByNameEntity(name)
                .map(this::mapToDomain)
                .doOnSuccess(player -> log.info("Jugador '{}' encontrado correctamente.", player))
                .doOnError(error -> log.error("Error al buscar al jugador '{}': {}.", name, error.getMessage()));
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
                .map(this::mapToDomain)
                .doOnSuccess(saved -> log.info("Jugador: '{}' guardado correctamente.", saved))
                .doOnError(error -> log.error("Error al guardar el jugador: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> delete(Player player) {
        return mysql.delete(mapToJpa(player));
    }

    @Override
    public Flux<Player> findAll() {
        return mysql.findAll()
                .map(this::mapToDomain);
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
