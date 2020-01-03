package io.blocko.exec;

import java.math.BigInteger;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import io.blocko.model.Answer;
import io.blocko.model.Survey;
import io.blocko.repositoy.AnswerRepository;
import io.blocko.repositoy.SurveyRepository;

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
    surveyRepository.save(survey);
  }
}
