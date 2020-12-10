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

    public Player(){
        active = true;
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


    public JSONObject toJSONObject() {
        JSONObject player = new JSONObject();
        player.put("id", id);
        player.put("name", name);
        player.put("imageIndex", imageIndex);
        player.put("host", host);
        return player;
    }

}
