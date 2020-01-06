package io.blocko.controller;

import io.blocko.repositoy.SurveyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {

  @Autowired
  private SurveyRepository surveyRepository;

  @GetMapping("/")
  public ModelAndView defaultHome(ModelAndView mav) {
    surveyRepository.findAll().forEach((it) -> {
      log.info("defaultHome() - {}", it);
    });
    mav.setViewName("index");
    mav.addObject("list", surveyRepository.findAll());
    return mav;
  }
}
