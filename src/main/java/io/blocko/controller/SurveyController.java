package io.blocko.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import io.blocko.repositoy.SurveyRepository;

@Controller
@RequestMapping("/surveys")
public class SurveyController {

  @Autowired
  private SurveyRepository surveyRepository;
  
  @GetMapping
  public ModelAndView detail(@RequestParam("id") Long id) {
    return surveyRepository.findById(id).map(it -> new ModelAndView("detail", "survey", it))
        .orElse(new ModelAndView("index"));
  }
}
