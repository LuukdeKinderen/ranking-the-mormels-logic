package nl.luukdekinderen.rankingthemormels.models;

import org.json.JSONObject;

public class Player {

    private String id;
    private String name;
    private int imageIndex;
    private int drinkCount;
    private Boolean host;
    private Boolean active;
    private Ranking ranking;
    private Integer rankingScore;

    public Player(){
        active = true;
        rankingScore = 0;
    }

    public Player(String id, String name, Boolean host) {
        this.id = id;
        this.name = name;
        this.host = host;
        active = true;
        rankingScore = 0;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isHost() {
        return host;
    }

    public int getDrinkCount() {
        return drinkCount;
    }

    public Ranking getRanking() {
        return ranking;
    }

    public void addDrinkCount(int drinkCount) {
        this.drinkCount += drinkCount;
    }

    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }

    public void setImageIndex(int index) {
        imageIndex = index;
    }

    public Integer getImageIndex(){return imageIndex;}

    public JSONObject toJSONObject() {
        JSONObject player = new JSONObject();
        player.put("id", id);
        player.put("name", name);
        player.put("imageIndex", imageIndex);
        player.put("host", host);
        player.put("drinkCount", drinkCount);
        return player;
    }

    public void addRankingScore(Integer score){
        rankingScore += score;
    }

    public void resetRankingScore() {
        rankingScore = 0;
    }

    public Integer getRankingScore(){
        return rankingScore;
    }
}
