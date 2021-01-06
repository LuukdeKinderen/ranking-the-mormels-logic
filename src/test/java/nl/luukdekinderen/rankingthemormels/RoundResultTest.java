package nl.luukdekinderen.rankingthemormels;


import nl.luukdekinderen.rankingthemormels.models.Player;
import nl.luukdekinderen.rankingthemormels.models.Question;
import nl.luukdekinderen.rankingthemormels.models.Ranking;
import nl.luukdekinderen.rankingthemormels.models.RoundResult;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RoundResultTest {

    private List<Player> players;
    private Question question;

    private void addPlayers(int count) {
        players = new ArrayList<>();
        for (Integer i = 0; i < count; i++) {

            String str = i.toString();
            players.add(new Player(str, str, false));
        }
    }

    private void addRandomDrinkCount() {
        Random r = new Random();
        for (Player p : players) {
            p.addDrinkCount(r.nextInt(10));
        }
    }

    private void addRandomRanking() {
        Random r = new Random();
        for (Player p : players) {
            Ranking randomRanking = new Ranking(
                    players.get(r.nextInt(players.size())).getId(),
                    players.get(r.nextInt(players.size())).getId(),
                    players.get(r.nextInt(players.size())).getId(),
                    players.get(r.nextInt(players.size())).getId()
            );

            p.setRanking(randomRanking);
        }
    }

    private void setQuestion() {
        question = new Question(Long.valueOf(1), "test", "FIRST: %s, is the name,%d is the drinkcount", "LAST: %s, is the name,%d is the drinkcount");
    }

    @Test
    public void PlayerDrinkCountIsIncreasedWhileMakingRoundResult() {
        //arrange
        addPlayers(10);
        addRandomRanking();
        setQuestion();

        //act
        RoundResult roundResult = new RoundResult(players, question);

        //assert
        assertThat(roundResult.getIsForPlayer().getDrinkCount() > 0).isTrue();
    }

    @Test
    public void PlayerNameIsShownInTask() {
        //arrange
        addPlayers(10);
        addRandomRanking();
        setQuestion();

        //act
        RoundResult roundResult = new RoundResult(players, question);
        String playerName = roundResult.getIsForPlayer().getName();
        String task = roundResult.getTask();

        //assert
        assertThat(task.contains(playerName)).isTrue();
    }

    @Test
    public void LowestDrinkCountGetsTask() {
        //arrange
        addPlayers(10);
        addRandomDrinkCount();
        addRandomRanking();
        setQuestion();

        //act
        RoundResult roundResult = new RoundResult(players, question);
        Player player = roundResult.getIsForPlayer();
        Boolean first = roundResult.getAverageRanking().indexOf(player) == 0;

        Integer playerOriginalDrinkCount = player.getDrinkCount() - roundResult.getDrinkCount();
        Integer otherDrinkCount = roundResult.getAverageRanking().get(first ? 3 : 0).getDrinkCount();

        //assert
        assertThat(playerOriginalDrinkCount).isLessThan(otherDrinkCount);
    }

    @Test
    public void AverageIsAccurate() {
        //arrange
        addPlayers(5);
        setQuestion();
        String expectedId = "1";

        //addRanking
        for (Player p : players) {
            Ranking ranking = new Ranking("1", "1", "1", "2");
            p.setRanking(ranking);
        }

        //act
        RoundResult roundResult = new RoundResult(players,question);

        String resultId = roundResult.getIsForPlayer().getId();

        //assert
        assertThat(resultId).isEqualTo(expectedId);

    }
}