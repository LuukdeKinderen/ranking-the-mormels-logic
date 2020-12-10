package nl.luukdekinderen.rankingthemormels.models;

import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class Result {
    private List<Player> players;
    private List<Player> result;
    private Player isFor;
    private String task;

    public Result(List<Player> players, Question question) {
        this.players = players;

        Ranking averageRanking = calculateAverageRanking();

        result = rankingToPlayerList(averageRanking);

        Boolean isForFirst = taskIsForFirst(averageRanking);
        isFor = isForFirst ? result.get(0) : result.get(3);

        String annotation;
        if (isForFirst) {
            annotation = question.getFirstAnnotation();
        } else {
            annotation = question.getLastBestAnnotation();
        }

        // random int between 2 and 5
        Integer drinkCount = new Random().nextInt(3) + 2;
        task = String.format(annotation, drinkCount);


        Player player = getPlayer(players,isFor.getId());
        player.addDrinkCount(drinkCount);
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("player", isFor.toJSONObject());
        json.put("annotation", task);


        List<JSONObject> resultObjs = result.stream()
                .map(Player::toJSONObject)
                .collect(Collectors.toList());

        json.put("result", resultObjs);
        return json;
    }


    private Player getPlayer(List<Player> players, String id) {
        Player foundPlayer = players.stream()
                .filter(player -> player.getId().equals(id))
                .findAny()
                .orElse(null);
        return foundPlayer;
    }

    private List<Player> rankingToPlayerList(Ranking ranking) {
        List<Player> playerRanking = new ArrayList<Player>();

        playerRanking.add(getPlayer(players, ranking.getFirstId()));
        playerRanking.add(getPlayer(players, ranking.getSecondId()));
        playerRanking.add(getPlayer(players, ranking.getThirdId()));
        playerRanking.add(getPlayer(players, ranking.getLastBestId()));

        return playerRanking;
    }

    private Ranking calculateAverageRanking() {

        Map<String, PlayerScore> playerPoints = new HashMap<>(4);


        //Fill player ranking points list
        for (Player player : players) {
            String playerId = player.getId();
            playerPoints.put(playerId, new PlayerScore(playerId));
        }

        for (Player player : players) {
            Ranking ranking = player.getRanking();

            playerPoints.get(ranking.getFirstId()).addToScore(5);
            playerPoints.get(ranking.getSecondId()).addToScore(3);
            playerPoints.get(ranking.getThirdId()).addToScore(1);
            playerPoints.get(ranking.getLastBestId()).addToScore(-2);
        }

        List<PlayerScore> playerScores = new ArrayList<>(playerPoints.values());
        playerScores.sort(Comparator.comparing(PlayerScore::getScore));

        Ranking ranking = new Ranking(
                playerScores.get(playerScores.size() - 1).getId(),
                playerScores.get(2).getId(),
                playerScores.get(1).getId(),
                playerScores.get(0).getId()
        );
        return ranking;
    }

    private Boolean taskIsForFirst(Ranking ranking) {

        Player first = getPlayer(players, ranking.getFirstId());
        Player last = getPlayer(players, ranking.getLastBestId());

        return first.getDrinkCount() < last.getDrinkCount();
    }

}
