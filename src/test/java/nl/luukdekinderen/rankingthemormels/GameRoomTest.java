package nl.luukdekinderen.rankingthemormels;


import nl.luukdekinderen.rankingthemormels.models.GameRoom;
import nl.luukdekinderen.rankingthemormels.models.Player;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GameRoomTest {


    @Test
    public void addPlayerWorks() throws Exception {
        //arrange
        GameRoom gameRoom = new GameRoom();
        Player player = new Player("test", "test", true);

        //act
        Boolean joinStatus = gameRoom.addPlayer(player);

        //assert
        assertThat(gameRoom.getPlayers().contains(player)).isTrue();
        assertThat(joinStatus).isTrue();
    }

    @Test
    public void addPlayerFailsIfNameAlreadyExists() throws Exception {
        //arrange
        GameRoom gameRoom = new GameRoom();
        Player player = new Player("id1", "test", true);
        gameRoom.addPlayer(player);
        Player playerTwo = new Player("id2", "test", false);

        //act
        Boolean joinStatus = gameRoom.addPlayer(playerTwo);

        //assert
        assertThat(gameRoom.getPlayers().contains(playerTwo)).isFalse();
        assertThat(joinStatus).isFalse();
    }

    @Test
    public void PlayerImageIndexIsUnique() {
        //arrange
        GameRoom gameRoom = new GameRoom();
        for (Integer i = 0; i < 9; i++) {
            String str = i.toString();
            gameRoom.addPlayer(new Player(str, str, false));
        }

        Player player = new Player("id", "test", true);

        //act
        gameRoom.addPlayer(player);

        //assert
        Integer imageIndex = player.getImageIndex();
        assertThat(gameRoom.getPlayers().stream().allMatch(p -> !p.getImageIndex().equals(imageIndex))).isFalse();


    }

    @Test
    public void PlayerElevenCanNotJoin() {
        //arrange
        GameRoom gameRoom = new GameRoom();
        for (Integer i = 0; i < 10; i++) {
            String str = i.toString();
            gameRoom.addPlayer(new Player(str, str, false));
        }

        Player player = new Player("id", "test", true);

        //act
        Boolean joinStatus = gameRoom.addPlayer(player);

        //assert
        assertThat(joinStatus).isFalse();
    }
}