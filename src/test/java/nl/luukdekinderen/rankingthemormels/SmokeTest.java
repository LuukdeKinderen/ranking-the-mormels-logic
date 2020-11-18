package nl.luukdekinderen.rankingthemormels;


import static org.assertj.core.api.Assertions.assertThat;

import nl.luukdekinderen.rankingthemormels.resources.RoomController;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SmokeTest {

	@Autowired
	private RoomController controller;

	@Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

	@Test
	public void Test() throws Exception {
		assertThat(true).isEqualTo(true);
	}
}