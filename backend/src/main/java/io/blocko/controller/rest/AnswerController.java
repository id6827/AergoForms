package io.blocko.controller.rest;

import io.blocko.controller.AbstractController;
import io.blocko.model.Answer;
import io.blocko.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/answers")
public class AnswerController extends AbstractController {

  @Autowired
  private AnswerService answerService;

  @GetMapping("{id}")
  public HttpEntity<Answer> detail(@PathVariable String id) {
    return answerService.getAnswer(id).map(it -> {
      logger.info("Answer : {}", it);
      return ResponseEntity.ok(it);
    }).orElse(ResponseEntity.noContent().build());
  }

  @PutMapping("{id}")
  public HttpEntity<Answer> increment(@PathVariable String id) {
    return answerService.increment(id).map(ResponseEntity::ok)
        .orElse(ResponseEntity.noContent().build());
  }

}
