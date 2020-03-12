package io.blocko.controller.rest;

import io.blocko.controller.AbstractController;
import io.blocko.model.Form;
import io.blocko.model.Survey;
import io.blocko.service.SurveyService;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/surveys")
public class SurveyController extends AbstractController {

  @Autowired
  private SurveyService surveyService;

  @GetMapping("{id}")
  public HttpEntity<Survey> detail(@PathVariable String id) {
    return surveyService.getSurvey(id).map(it -> {
      logger.info("survey : {}", it);
      return ResponseEntity.ok(it);
    }).orElse(ResponseEntity.noContent().build());
  }

  @GetMapping
  public HttpEntity<List<Survey>> list(
      @PageableDefault(size = 10, direction = Direction.ASC) Pageable pageable) {
    final List<Survey> contents = surveyService.listPublicSurveys(pageable).getContent();
    logger.info("survey list : {}", contents);
    return contents.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(contents);
  }
  
  @PostMapping
  @Secured("ROLE_ADMIN")
  @Transactional
  public HttpEntity<Survey> submit(Form form){
    return ResponseEntity.ok(surveyService.register(form));
  }

}
