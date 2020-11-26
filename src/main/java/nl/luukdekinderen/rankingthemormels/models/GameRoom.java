package nl.luukdekinderen.rankingthemormels.models;


import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public class GameRoom {

    private String id;
    private List<Player> players;
    private int currentQuestionCount;
    private Question[] questions;

    public GameRoom() {
        currentQuestionCount = -1;
    }

    public String getId() {
        return id;
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
    public Player getPlayer(String id) {
        Player foundPlayer = players.stream()
                .filter(player -> player.getId().equals(id))
                .findAny()
                .orElse(null);
        return foundPlayer;
    }
    public List<Player> getPlayers() {
        return players.stream()
                .filter(p -> p.isActive())
                .collect(Collectors.toList());
    }
    public Boolean getIsAllInActive (){

       return players.stream().allMatch(p -> !p.isActive());
    }
    public Boolean isRealHost(String playerId) {
        for (Player player : players) {
            if (player.getId().equals(playerId) && player.isHost()) {
                return true;
            }
        }
        return false;
    }
    public void setPlayerActiveStatus(String id, Boolean active){
        Player player = getPlayer(id);
        player.setActive(active);
    }

    public String getQuestion() {
        if (currentQuestionCount > -1) {
            Question question = questions[currentQuestionCount];
            return question.getQuestion();
        }
        return null;
    }
    public void nextQuestion() {
        currentQuestionCount++;
        for (Player player : players) {
            player.setRanking(null);
        }
    }
    public void addRanking(String playerId, Ranking ranking) {
        Player player = getPlayer(playerId);
        player.setRanking(ranking);
    }
    public void setQuestions(Question[] questions) {
        this.questions = questions;
    }

    public Boolean isDoneRanking(){
        Boolean isAllDone = players.stream().allMatch(player -> player.getRanking()!=null);
        return isAllDone;
    }









}
