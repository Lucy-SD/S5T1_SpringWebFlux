package blackjack.aahhrefact.module.player.infrastructure.persistance;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PlayerRepositoryMySQL extends R2dbcRepository<PlayerJpaEntity, Long> {

    @Query("SELECT * FROM player WHERE name = :name")
    Mono<PlayerJpaEntity> findByNameEntity(@Param("name") String name);

    @Query("SELECT EXISTS(SELECT 1 FROM player WHERE name = :name)")
    Mono<Boolean> existsByNameEntity(@Param("name") String name);
}
