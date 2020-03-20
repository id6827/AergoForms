package io.blocko.exec;

import io.blocko.model.Answer;
import io.blocko.model.Survey;
import io.blocko.model.SurveyType;
import io.blocko.repositoy.AnswerRepository;
import io.blocko.repositoy.SurveyRepository;
import java.math.BigInteger;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("io.blocko")
public class WebApplicationLauncher implements CommandLineRunner {
  public static void main(String[] args) {
    SpringApplication.run(WebApplicationLauncher.class, args);
  }

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private AnswerRepository answerRepository;

  @Override
  public void run(String... args) throws Exception {
    final String question = "Are you a developer?";
    final LocalDateTime startTime = LocalDateTime.now();
    final LocalDateTime endTime = LocalDateTime.now();
    final Answer answer = new Answer("YES", BigInteger.TEN);
    final Answer otherAnswer = new Answer("NO", BigInteger.ONE);
    answerRepository.save(answer);
    answerRepository.save(otherAnswer);
    final Survey survey = new Survey(question, startTime, endTime, answer, otherAnswer);
    survey.setType(SurveyType.Public);
    surveyRepository.save(survey);

    final String question1 = "아르고 대통령 국정 수행 지지율";
    final LocalDateTime startTime1 = LocalDateTime.now();
    final LocalDateTime endTime1 = LocalDateTime.now();
    final Answer answer1 = new Answer("긍정", BigInteger.valueOf(50L));
    final Answer theOtherAnswer1 = new Answer("부정", BigInteger.valueOf(50L));
    answerRepository.save(answer1);
    answerRepository.save(theOtherAnswer1);
    final Survey survey1 = new Survey(question1, startTime1, endTime1, answer1, theOtherAnswer1);
    survey1.setType(SurveyType.Private);
    surveyRepository.save(survey1);

  }
}
