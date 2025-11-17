package blackjack.shared.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories (
        basePackages = "blackjack.module.player.infrastructure.persistence"
)
public class R2dbcConfig {
}
