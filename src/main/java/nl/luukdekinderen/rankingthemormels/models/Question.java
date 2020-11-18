package nl.luukdekinderen.rankingthemormels.models;

public class Question {
    enum AnnotationLevel {
        first,
        second,
        third,
        lastBest
    }

    private int id;
    private String question;
    private String firstPersAnnotation;
    private String secondPersAnnotation;
    private String thirdPersAnnotation;
    private String lastBestAnnotation;

    public int getQuestionId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnnotation(AnnotationLevel annotationLevel) {
        switch (annotationLevel) {
            case first:
                return firstPersAnnotation;
            case second:
                return secondPersAnnotation;
            case third:
                return thirdPersAnnotation;
            case lastBest:
                return lastBestAnnotation;
            default:
                return null;
        }
    }

}
