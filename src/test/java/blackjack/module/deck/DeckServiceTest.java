//package blackjack.deck;
//
//import blackjack.deck.application.service.DeckService;
//import blackjack.entity.domain.deck.module.Card;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.junit.jupiter.MockitoExtension;
//import reactor.core.publisher.Flux;
//import reactor.test.StepVerifier;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(MockitoExtension.class)
//class DeckServiceTest {
//
//    @InjectMocks
//    private DeckService deckService;
//
//    @Test
//    void createShuffledDeck_returnsFluxOf52ShuffledCards() {
//        Flux<Card> deck = deckService.createShuffledDeck();
//        Flux<Card> deck2 = deckService.createShuffledDeck();
//
//        StepVerifier.create(deck)
//                .expectNextCount(52)
//                .verifyComplete();
//
//        List<Card> shuffled1 = deck.collectList().block();
//        List<Card> shuffled2 = deck2.collectList().block();
//
//        assertThat(shuffled1).isNotEqualTo(shuffled2);
//    }
//}
