package nl.luukdekinderen.rankingthemormels.models;


import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public class GameRoom {

    private String id;
    private List<Player> players;
    private int currentQuestionCount;
    private String[] questionIds;

    public GameRoom(){
        currentQuestionCount = -1;
        //TODO use Question service to fill questionIds
    }

    public boolean AddPlayer(Player newPlayer) {
        boolean flag = false;
        for (Player player : players) {
            if (newPlayer.getName().equals(player.getName())) {
                flag = true;
            }
        }
        if (!flag) {
            newPlayer.setImageIndex(players.size());
            players.add(newPlayer);
            return true;
        }
        return false;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getQuestion() {
        //TODO use Question service

        if (currentQuestionCount > -1) {
            return "Wie kan het meeste bier op";
        }
        return null;
    }


    public void nextQuestion() {
        currentQuestionCount++;
        //check for end of game

        //send new question to frondEnd

        //clear all player rankings

        //start timer
        // after timer => SendAverageRanking()

    }


    // Do on every player Ranking update
    public void DidFinishRankings() {
        // check if all players finished sending ranking

        // if true
        // stopTimer
        // SendRanking()

    }

    private void SendAverageRanking() {
        // CalculateAverageRanking()
        // CreateTaskForRanking()
        // Send task to frontEnd
    }

    private Ranking CalculateAverageRanking() {
        return new Ranking("test", "test", "test", "test");
    }

    private String CreateTaskForRanking(Ranking ranking) {
        return "test";
    }


    public String getId() {
        return id;
    }

    public List<JSONObject> getPlayerObjects() {
        List<JSONObject> playerObjs = players.stream()
                .map(Player::toJSONObject)
                .collect(Collectors.toList());
        return playerObjs;
    }

    public JSONObject getPlayerObject(String id) {
        Player foundPlayer = players.stream()
                .filter(player -> player.getId().equals(id))
                .findAny()
                .orElse(null);
        if (foundPlayer != null) {
            return foundPlayer.toJSONObject();
        }
        return null;
    }

    public boolean isRealHost(String playerId) {
        for (Player player : players) {
            if (player.getId().equals(playerId) && player.isHost()) {
                return true;
            }
        }
        return false;
    }
}
