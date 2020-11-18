package nl.luukdekinderen.rankingthemormels.models;

public class Ranking {
    private String firstId;
    private String secondId;
    private String thirdId;
    private String lastBestId;

    public Ranking(String firstId, String secondId, String thirdId, String lastBestId) {
        this.firstId = firstId;
        this.secondId = secondId;
        this.thirdId = thirdId;
        this.lastBestId = lastBestId;
    }

    public String getFirstId() {
        return firstId;
    }

    public String getSecondId() {
        return secondId;
    }

    public String getThirdId() {
        return thirdId;
    }

    public String getLastBestId() {
        return lastBestId;
    }
}
