package io.blocko.controller.rest;

import io.blocko.controller.AbstractController;
import io.blocko.model.Event;
import io.blocko.service.account.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController extends AbstractController {

  @Autowired
  protected UserService userService;

  @PostMapping("event")
  public void handle(@RequestBody final Event event) throws Exception {
    logger.info("Event : {} {}", event);
    userService.handle(event);
  }

}
