package nl.luukdekinderen.rankingthemormels;


import static org.assertj.core.api.Assertions.assertThat;

import nl.luukdekinderen.rankingthemormels.controllers.RoomController;
import nl.luukdekinderen.rankingthemormels.models.GameRoom;
import nl.luukdekinderen.rankingthemormels.models.Player;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

}