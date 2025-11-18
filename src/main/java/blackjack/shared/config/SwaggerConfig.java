package blackjack.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI blackjackOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BlackJack API")
                        .description("API Reactiva para jugar al BlackJack con Srping WebFlux.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Lucy SD")
                                .email("myemail@example.com")));
    }
}
