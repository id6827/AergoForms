package io.blocko.service;

import static java.util.Optional.empty;

import io.blocko.model.Answer;
import io.blocko.model.Vote;
import io.blocko.model.account.User;
import io.blocko.repositoy.AnswerRepository;
import io.blocko.repositoy.SurveyRepository;
import io.blocko.repositoy.VoteRepository;
import io.blocko.service.account.UserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerService extends AbstractService {

  @Autowired
  protected SurveyRepository surveyRepository;

  @Autowired
  protected AnswerRepository answerRepository;

  @Autowired
  protected UserService userService;

  @Autowired
  protected VoteRepository voteRepository;


  public Optional<Answer> increment(final String id) {
    final Optional<User> currentUserOpt = userService.getCurrentUser();
    if (!currentUserOpt.isPresent()) {
      return empty();
    }

    final Optional<Answer> answerOpt = answerRepository.findById(id);
    if (!answerOpt.isPresent()) {
      return empty();
    }

    final User user = currentUserOpt.get();
    final Answer answer = answerOpt.get();
    
    //테이블 잘짜면 굳이 이거 안해도 될듯 ?...
    final Optional<Vote> voteOpt =
        voteRepository.findByVoterIdAndAnswerId(user.getId(), answer.getId());
    
    if(voteOpt.isPresent()) {
      logger.info("Duplicated vote");
      return empty();
    }
    
    logger.info("Before increment answer: {}", answer);
    answer.inc();
    logger.info("After increment answer : {}", answer);
    answerRepository.save(answer);

    final Vote vote = new Vote(user, answer);
    logger.info("Vote : {}", vote);
    voteRepository.save(vote);

    return answerOpt;
  }

  public Optional<Answer> getAnswer(final String id) {
    return answerRepository.findById(id);
  }


}
