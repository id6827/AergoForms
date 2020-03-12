package io.blocko.service;

import static io.blocko.model.SurveyType.Private;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;

import io.blocko.model.Answer;
import io.blocko.model.Form;
import io.blocko.model.Survey;
import io.blocko.model.SurveyType;
import io.blocko.model.account.User;
import io.blocko.repositoy.AnswerRepository;
import io.blocko.repositoy.SurveyRepository;
import io.blocko.service.account.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SurveyService extends AbstractService {

  @Autowired
  protected SurveyRepository surveyRepository;

  @Autowired
  protected AnswerRepository answerRepository;

  @Autowired
  protected UserService userService;


  public Optional<Survey> getSurvey(final String id) {
    final Optional<Survey> surveyOpt = surveyRepository.findById(id);
    if (!surveyOpt.isPresent()) {
      return empty();
    }

    logger.trace("Check if private survey");
    final Survey survey = surveyOpt.get();

    if (Private == survey.getType()) {
      // final Optional<User> currentUserOpt = userService.getCurrentUser();
      // if (!currentUserOpt.isPresent()) {
      // return empty();
      // }
      // final User currentUser = currentUserOpt.get();
      // logger.debug("Current user: {}", currentUser);
      // private 설문에 관련된 사용자인지 체크하고 넘겨줘야함.
      return empty();
    }

    return surveyOpt;
  }

  public Page<Survey> listPublicSurveys(Pageable pageable) {
    return surveyRepository.findByType(SurveyType.Public.code(), pageable);
  }

  public Survey register(Form form) {
    // String question, byte type, LocalDateTime startTime, LocalDateTime endTime,
    // Answer... answers
    final List<Answer> answers = form.getItems().stream().map(i -> {
      final Answer answer = new Answer(i.getText(), i.getCount());
      return answerRepository.save(answer);
    }).collect(toList());

    final User currentUser = userService.getCurrentUser().orElseThrow(IllegalStateException::new);
    logger.info("Current User : {}", currentUser);
    final Survey survey = new Survey(form.getQuestion(), form.getStartTime(), form.getEndTime());
    survey.setAnswers(answers);
    survey.setType(SurveyType.fromCode(form.getType()));
    survey.setOwner(currentUser);

    logger.info("Survey : {}", survey);
    surveyRepository.save(survey);
    return survey;
  }

}
