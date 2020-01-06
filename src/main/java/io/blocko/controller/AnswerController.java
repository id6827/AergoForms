package io.blocko.controller;

import io.blocko.repositoy.AnswerRepository;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/")
public class AnswerController {
  
  @Autowired
  private AnswerRepository answerRepository;
  
  @GetMapping("/answers")
  public ModelAndView test(HttpServletRequest request) {
    final Long id = Long.valueOf(request.getParameter("optradio"));
    answerRepository.findById(id).ifPresent(answer->{
      log.info("Answer before Inc: {}", answer);
      answer.inc();
      log.info("Answer after Inc: {}", answer);
      answerRepository.save(answer);
    });
    return new ModelAndView("redirect:/");
  }
}
