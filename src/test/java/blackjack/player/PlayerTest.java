//package blackjack.player;
//
//import blackjack.aahhrefact.module.player.domain.entity.Player;
//import org.junit.jupiter.api.Test;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class PlayerTest {
//
//    @Test
//    void newPlayer_hasEmptyHandAndZeroScore() {
//        Player player = Player.builder().name("Pepe").build();
//
//        assertThat(player.getName()).isEqualTo("Pepe");
//        assertThat(player.getGamesWon()).isZero();
//        assertThat(player.getGamesLost()).isZero();
//        assertThat(player.getGamesPushed()).isZero();
//    }
//
//    @Test
//    void playerIncrementsStats() {
//        Player player = Player.builder().name("Pepe").build();
//
//        player.increaseGamesWon();
//        assertThat(player.getGamesWon()).isEqualTo(1);
//
//        player.increaseGamesLost();
//        assertThat(player.getGamesLost()).isEqualTo(1);
//
//        player.increaseGamesPushed();
//        assertThat(player.getGamesPushed()).isEqualTo(1);
//    }
//
//
//}
