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
       Question[] questions = new Question[questionCount];

        for (Integer i = 0; i < questionCount; i++) {
            Question question = restTemplate.getForObject("http://ranking-the-mormels-questions/question/" + i, Question.class);

            questions[i] = question;
        }
        return questions;
    }

    public Question getQuestion(Integer id) {
        Question question = restTemplate.getForObject("http://ranking-the-mormels-questions/question/" + id, Question.class);
        return question;
    }


}
