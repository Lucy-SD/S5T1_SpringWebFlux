package blackjack.shared.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import reactor.core.publisher.Mono;


@Slf4j
@Configuration
public class DatabaseInitializer {

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {


        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("init.sql"));
        populator.setContinueOnError(true);

        initializer.setDatabasePopulator(populator);
        return initializer;
    }

    @Bean
    public CommandLineRunner initDadabase(R2dbcEntityTemplate template) {
        return args -> {

            template.getDatabaseClient().sql("SELECT 1 FROM player LIMIT 1")
                    .fetch()
                    .rowsUpdated()
                    .onErrorResume(e -> {
                        log.info("No existe la tabla de jugador. Ejecutando script de inicilización . . .");
                        return Mono.empty();
                    })
                    .subscribe(
                            result -> log.info("Base de datos verificada correctamente."),
                            error -> log.error("Error verificando base de datos: {}", error.getMessage()),
                            () -> log.info("Inicialización de base de datos completa = )")
                    );
        };
    }
}
