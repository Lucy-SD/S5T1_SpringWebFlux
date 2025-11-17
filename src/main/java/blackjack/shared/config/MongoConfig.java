package blackjack.shared.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(
        basePackages = "blackjack.module.game.infrastructure.persistence"
)
public class MongoConfig {
}
