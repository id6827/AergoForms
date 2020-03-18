package io.blocko.controller.rest;

import io.blocko.controller.AbstractController;
import io.blocko.model.Event;
import io.blocko.service.account.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController extends AbstractController {

  @Autowired
  protected UserService userService;

  @PostMapping("event")
  public void handle(String eventStr) throws JsonParseException, JsonMappingException, IOException {
    logger.debug("Evend : {}", eventStr);
    userService.handle(eventStr);
  }

}
