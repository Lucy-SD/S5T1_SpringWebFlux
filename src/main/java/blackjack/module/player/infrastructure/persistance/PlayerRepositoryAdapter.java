package blackjack.module.player.infrastructure.persistance;

import blackjack.module.player.domain.entity.Player;
import blackjack.module.player.domain.port.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class PlayerRepositoryAdapter implements PlayerRepository {

    private final PlayerRepositoryMySQL mysql;
    private static final Logger log = LoggerFactory.getLogger(PlayerRepositoryAdapter.class);

    public PlayerRepositoryAdapter(PlayerRepositoryMySQL mysql) {
        this.mysql = mysql;
    }

    @Override
    public Mono<Player> findByName(String name) {
        log.info("🔎 Buscando player por nombre en MySQL: {}", name);
        return mysql.findByNameEntity(name)
                .map(this::mapToDomain)
                .doOnSuccess(player -> log.info("✅ Player encontrado en MySQL: {}", player))
                .doOnError(error -> log.error("❌ Error en MySQL buscando '{}': {}", name, error.getMessage(), error));
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        log.info("🔍 Verificando existencia de player: {}", name);
        return mysql.existsByNameEntity(name)
                .doOnSuccess(exists -> log.info("📊 Resultado existencia '{}': {}", name, exists))
                .doOnError(error -> log.error("❌ Error verificando existencia '{}': {}", name, error.getMessage(), error));
    }

    @Override
    public Mono<Player> findById(Long id) {
        return mysql.findById(id)
                .map(this::mapToDomain);
    }

    @Override
    public Mono<Player> save(Player player) {
        log.info("💾 Guardando player: {}", player);
        PlayerJpaEntity entity = mapToJpa(player);
        return mysql.save(entity)
                .map(this::mapToDomain)
                .doOnSuccess(saved -> log.info("✅ Player guardado: {}", saved))
                .doOnError(error -> log.error("❌ Error guardando player: {}", error.getMessage(), error));
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
