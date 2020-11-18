package nl.luukdekinderen.rankingthemormels.resources;

import nl.luukdekinderen.rankingthemormels.models.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ranking-the-mormels")
public class RankingTheMormels {


    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/{questionCount}")
    public List<Question> getQuestions(@PathVariable("questionCount")int questionCount) {

        List<Question> questions = new ArrayList<>();

        for (int i =0; i<questionCount;i++){

            Question question = new Question(); //restTemplate.getForObject("http://ranking-questions/question/" + i , Question.class);

            questions.add(question);
        }
        return questions;
    }

    public Question getQuestion(int index){
        Question question = restTemplate.getForObject("http://ranking-questions/question/"+index , Question.class);
        return question;
    }

}
