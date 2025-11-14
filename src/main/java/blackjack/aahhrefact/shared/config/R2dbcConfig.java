package blackjack.aahhrefact.shared.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories (
        basePackages = "blackjack.aahhrefact.module.player.infraestructure.persistance"
)
public class R2dbcConfig {
}
