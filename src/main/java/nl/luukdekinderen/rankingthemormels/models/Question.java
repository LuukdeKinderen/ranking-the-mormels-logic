package nl.luukdekinderen.rankingthemormels.models;

public class Question {

    private Integer questionId;
    private String question;
    private String firstPersAnnotation;
    private String lastBestAnnotation;

    public Question() {

    }

    public String getQuestion() {
        return question;
    }

    public String getFirstPersAnnotation(){
        return firstPersAnnotation;
    }

    public String getLastBestAnnotation(){
        return lastBestAnnotation;
    }
}

