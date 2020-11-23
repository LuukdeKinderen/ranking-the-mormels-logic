package nl.luukdekinderen.rankingthemormels.resources;

import nl.luukdekinderen.rankingthemormels.models.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class QuestionService {

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    public void QuestionService() {

    }

    public Question[] getQuestions(Integer questionCount) {
        Question[] questions = restTemplate.getForObject("https://ranking-the-mormels-questions.herokuapp.com/question/random/" + questionCount, Question[].class);
        return questions;
    }

    public Question getQuestion(Integer id) {
        Question question = restTemplate.getForObject("https://ranking-the-mormels-questions.herokuapp.com/question/" + id, Question.class);
        return question;
    }


}
