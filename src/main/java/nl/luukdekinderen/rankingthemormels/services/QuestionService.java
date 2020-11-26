package nl.luukdekinderen.rankingthemormels.services;

import nl.luukdekinderen.rankingthemormels.models.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class QuestionService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${questionservice.uri}")
    private String questionUri;

    @Autowired
    public void QuestionService() {

    }

    public Question[] getQuestions(Integer questionCount) {
        Question[] questions = restTemplate.getForObject(questionUri + "/question/random/" + questionCount, Question[].class);
        return questions;
    }

    public Question getQuestion(Integer id) {
        Question question = restTemplate.getForObject(questionUri + "/question/" + id, Question.class);
        return question;
    }


}
