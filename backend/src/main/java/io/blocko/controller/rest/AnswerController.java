package io.blocko.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.blocko.model.Answer;
import io.blocko.repositoy.AnswerRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/answers")
public class AnswerController {

  @Autowired
  private AnswerRepository answerRepository;

  @GetMapping("{id}")
  public HttpEntity<Answer> detail(@PathVariable Long id) {
    return answerRepository.findById(id).map(it -> {
      log.info("Answer : {}", it);
      return ResponseEntity.ok(it);
    }).orElse(ResponseEntity.noContent().build());
  }
  
  @PutMapping("{id}")
  public HttpEntity<Answer> increment(@PathVariable Long id) {
    return answerRepository.findById(id).map(it -> {
      log.info("Answer before Inc: {}", it);
      it.inc();
      log.info("Answer after Inc: {}", it);
      answerRepository.save(it);
      return ResponseEntity.ok(it);
    }).orElse(ResponseEntity.noContent().build());
  }

}
