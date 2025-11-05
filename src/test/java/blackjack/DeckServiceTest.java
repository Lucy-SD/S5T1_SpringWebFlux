package blackjack;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class DeckServiceTest {

    @InjectMocks
    private DeckService deckService;

    @Test
    void createShuffledDeck_shouldGenerateA52CardsDeck() {

        Flux<Card> deck = deckService.createShuffledDeck();

        StepVerifier.create(deck)
                .expectNextCount(52)
                .verifyComplete();
    }




}
