package blackjack.aahhrefact.module.player.infrastructure.persistance;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PlayerRepositoryMySQL extends R2dbcRepository<PlayerJpaEntity, Long> {

    @Query("SELECT * FROM player WHERE name = :name")
    Mono<PlayerJpaEntity> findByNameEntity(@Param("name") String name);

    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM player WHERE name = :name")
    Mono<Boolean> existsByNameEntity(@Param("name") String name);

    @Query("""
            SELECT * FROM player
            WHERE (games_won + games_lost + games_pushed) > 0
            ORDER BY (games_won * 1.0 / (games_won + games_lost + games_pushed)) DESC
            LIMIT :limit
            """)
    Flux<PlayerJpaEntity> findTopPlayers(@Param("limit") int limit);
}
