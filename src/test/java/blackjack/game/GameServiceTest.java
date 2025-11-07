package blackjack.game;

import blackjack.deck.Card;
import blackjack.deck.DeckService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private DeckService deckService;

    @InjectMocks
    private GameService gameService;

    @Test
    void startNewGame_setsCorrectGameState() {
        List<Card> mockCards = Arrays.asList(
                new Card(10),
                new Card(7),
                new Card(8),
                new Card(10)
        );

        when(deckService.createShuffledDeck()).thenReturn(Flux.fromIterable(mockCards));

        Mono<GameState> gameStateMono = gameService.startNewGame("Pepe");
        GameState gameState = gameStateMono.block();

        assertThat(gameState.getPlayer().getHand()).hasSize(2);
        assertThat(gameState.getDealer().getHand()).hasSize(2);
        assertThat(gameState.getDealer().getVisibleCards()).hasSize(1);
        assertThat(gameState.getDealer().getHiddenCard()).isNotNull();

    }




}
