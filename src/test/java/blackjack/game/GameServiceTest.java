package blackjack.game;

import blackjack.deck.DeckService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private DeckService deckService;

    @InjectMocks
    private GameService gameService;

    @Test
    void startNewGame_setsCorrectGameState() {
        GameState gameState = gameService.startNewGame("Pepe");

        assertThat(gameState.getPlayer().getHand()).hasSize(2);
        assertThat(gameState.getDealer().getHand()).hasSize(2);
        assertThat(gameState.getDealer().getVisibleCards()).hasSize(1);
        assertThat(gameState.getDealer().getHiddenCards()).hasSize(1);

    }




}
