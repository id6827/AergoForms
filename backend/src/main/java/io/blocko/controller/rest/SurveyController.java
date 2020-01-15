package io.blocko.controller.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.blocko.model.Survey;
import io.blocko.repositoy.SurveyRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/surveys")
public class SurveyController {

  @Autowired
  private SurveyRepository surveyRepository;

  @GetMapping("{id}")
  public HttpEntity<Survey> detail(@PathVariable Long id) {
    return surveyRepository.findById(id).map(it -> {
      log.info("survey : {}", it);
      return ResponseEntity.ok(it);
    }).orElse(ResponseEntity.noContent().build());
  }

  @GetMapping
  public HttpEntity<List<Survey>> list(
      @PageableDefault(size = 10, direction = Direction.ASC) Pageable pageable) {
    final List<Survey> contents = surveyRepository.findAll(pageable).getContent();
    log.info("survey list : {}", contents);
    return contents.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(contents);
  }

}
