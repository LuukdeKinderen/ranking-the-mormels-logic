package nl.luukdekinderen.rankingthemormels.models;

public class PlayerScore {
    private String id;
    private Integer score;

    public PlayerScore(String id) {
        this.id = id;
        score = 0;
    }

    public String getId() {
        return id;
    }

    public Integer getScore() {
        return score;
    }

    public void addToScore(Integer score) {
        this.score += score;
    }
}
