package nl.luukdekinderen.rankingthemormels.models;

public class Question {

    private Long id;
    private String question;
    private String firstAnnotation;
    private String lastBestAnnotation;

    public Question() {

    }

    public Question(Long id, String question, String firstAnnotation, String lastBestAnnotation) {
        this.id = id;
        this.question = question;
        this.firstAnnotation = firstAnnotation;
        this.lastBestAnnotation = lastBestAnnotation;
    }

    public Long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getFirstAnnotation() {
        return firstAnnotation;
    }

    public String getLastBestAnnotation() {
        return lastBestAnnotation;
    }
}

