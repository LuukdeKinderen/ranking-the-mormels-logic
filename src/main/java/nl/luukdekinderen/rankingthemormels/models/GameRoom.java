package nl.luukdekinderen.rankingthemormels.models;


import java.util.*;
import java.util.stream.Collectors;

public class GameRoom {

    private String id;
    private List<Player> players;
    private int currentQuestionIndex;
    private Question[] questions;
    private Integer questionCount;

    public GameRoom() {
        currentQuestionIndex = -1;
        if (players == null) {
            players = new ArrayList<>();
        }
    }


    public String getId() {
        return id;
    }

    public boolean addPlayer(Player newPlayer) {
        if (players.size() == 10) {
            return false;
        }
        boolean flag = false;
        for (Player player : players) {
            if (newPlayer.getName().equals(player.getName())) {
                if (!player.isActive()) {
                    players.remove(player);
                    newPlayer.setImageIndex(player.getImageIndex());
                    players.add(newPlayer);
                }
                flag = true;
            }
        }
        if (!flag) {
            newPlayer.setImageIndex(getRandomPlayerImageIndex());
            players.add(newPlayer);
            return true;
        }
        return false;
    }

    private int getRandomPlayerImageIndex() {
        boolean found = false;

        Random rd = new Random();
        while (!found) {
            int index = rd.nextInt(10);
            found = true;
            for (Player p : players) {
                if (p.getImageIndex().equals(index)) {
                    found = false;
                }
            }
            if (found) {
                return index;
            }
        }
        return 0;
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

    public Boolean getIsAllInActive() {

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

    public void setPlayerActiveStatus(String id, Boolean active) {
        Player player = getPlayer(id);
        player.setActive(active);
    }

    public Question getQuestion() {
        if (currentQuestionIndex > -1) {
            Question question = questions[currentQuestionIndex];
            return question;
        }
        return null;
    }

    public Boolean nextQuestion() {
        for (Player player : players) {
            player.setRanking(null);
            player.resetRankingScore();
        }
        if (currentQuestionIndex < questions.length - 1) {
            currentQuestionIndex++;
            return true;
        } else {
            return false;
        }
    }

    public void addRanking(String playerId, Ranking ranking) {
        Player player = getPlayer(playerId);
        player.setRanking(ranking);
    }

    public void setQuestions(Question[] questions) {
        this.questions = questions;
    }

    public Boolean isDoneRanking() {
        Boolean isAllDone = players.stream().allMatch(player -> player.getRanking() != null);
        return isAllDone;
    }

    public RoundResult getRoundResult() {
        return new RoundResult(players, getQuestion());
    }

    public Integer getQuestionCount() {
        return questionCount;
    }
}
