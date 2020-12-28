package nl.luukdekinderen.rankingthemormels.models;

import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class RoundResult {
    private Player isForPlayer;
    private List<Player> averageRanking;
    private String task;

    public RoundResult(List< Player> players, Question question) {


        averageRanking = calculateAverageRanking(players);
        Boolean isForFirst = averageRanking.get(0).getDrinkCount() <= averageRanking.get(averageRanking.size() - 1).getDrinkCount();



        String annotation;
        if (isForFirst) {
            isForPlayer = averageRanking.get(0);
            annotation = question.getFirstAnnotation();
        } else {
            isForPlayer = averageRanking.get(averageRanking.size() - 1);
            annotation = question.getLastBestAnnotation();
        }

        //random Int between 2 and 5
        Integer drinkCount = new Random().nextInt(3) + 2;
        isForPlayer.addDrinkCount(drinkCount);
        task = String.format(annotation, isForPlayer.getName(), drinkCount);

    }

    private List<Player> calculateAverageRanking(List<Player> players) {


        for (Player player: players) {

            Ranking r = player.getRanking();

            getPlayer(players, r.getFirstId()).addRankingScore(5);
            getPlayer(players, r.getSecondId()).addRankingScore(3);
            getPlayer(players, r.getThirdId()).addRankingScore(1);
            getPlayer(players, r.getLastBestId()).addRankingScore(-1);
        }

        // only get active players and order by ranking score
        List<Player> playerList = players.stream()
                .filter(p -> p.isActive())
                .collect(Collectors.toList());
        playerList.sort(Comparator.comparing(Player::getRankingScore).reversed());


        return new ArrayList<Player>() {
            {
                add(playerList.get(0));
                add(playerList.get(1));
                add(playerList.get(2));
                add(playerList.get(playerList.size() - 1));
            }
        };
    }

    private Player getPlayer(List<Player> players, String id){
       return players.stream()
                .filter(player -> player.getId().equals(id))
                .findFirst()
                .get();
    }


    public JSONObject toJson(){

        JSONObject jsonObject = new JSONObject();

        List<JSONObject> resultObjs = averageRanking.stream()
                .map(Player::toJSONObject)
                .collect(Collectors.toList());


        jsonObject.put("result", resultObjs);

        jsonObject.put("player", isForPlayer.toJSONObject());
        jsonObject.put("task", task);

        return jsonObject;
    }

    public Player getIsForPlayer() {
        return isForPlayer;
    }

    public List<Player> getAverageRanking() {
        return averageRanking;
    }

    public String getTask() {
        return task;
    }
}


