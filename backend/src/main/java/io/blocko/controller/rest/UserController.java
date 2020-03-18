package io.blocko.controller.rest;

import static coinstack.paper.ServiceResult.success;
import static java.util.Objects.isNull;
import static java.util.UUID.randomUUID;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.blocko.controller.AbstractController;
import io.blocko.model.SignUpForm;
import io.blocko.model.account.User;
import io.blocko.service.account.UserService;
import lombok.Getter;
import lombok.Setter;

@RequestMapping("/users")
@RestController
public class UserController extends AbstractController {

  protected final ObjectMapper mapper = new ObjectMapper();

  @Getter
  @Setter
  @Autowired
  protected UserService userService;

  /**
   * Create user.
   *
   * @param usernameAndPassword username and password
   *
   * @return result
   */
  @PostMapping
  @Transactional
  public Object createNewUser(@RequestBody final SignUpForm signUpForm,
      HttpServletResponse response) throws Exception {
    logger.trace("Username: {}", signUpForm);
    userService.createNewUser(signUpForm);
    return success();
  }

  /**
   * Search usernames with partial word.
   *
   * @param partialUsername partial word
   * @param exceptMe flag if except current user.
   *
   * @return user informations.
   */
  @GetMapping
  public Object searchUsers(@RequestParam(value = "query") final String partialUsername,
      @RequestParam(value = "exceptMe", required = false) final boolean exceptMe) {
    logger.trace("Partial username: {}", partialUsername);

    // 수정 불가 리스트를 수정가능한 리스트로 바꾼다.
    final List<User> users = new ArrayList<>(userService.search(partialUsername));

    if (exceptMe) {
      userService.getCurrentUser().ifPresent(users::remove);
    }

    return success(users);
  }

  @PostMapping("/handle")
  public HttpEntity<String> handle(
      @AuthenticationPrincipal org.springframework.security.core.userdetails.User user, HttpSession session)
      throws JsonProcessingException {
    logger.info("user : {}", user);
    if (isNull(user)) {
      return ResponseEntity.badRequest().build();
    }
    session.setAttribute("user", user);
    final String uuid = randomUUID().toString();
    userService.execute(() -> {
      logger.info("UUID: {}", uuid);
      return uuid;
    });
    final Map<String, String> map = new HashMap<>();
    map.put("username", user.getUsername());
    map.put("uuid", uuid);
    return ResponseEntity.ok().body(mapper.writeValueAsString(map));
  }

  @PostMapping("/login")
  public HttpEntity<String> login(final String uuid, final String username, HttpSession session) throws JsonProcessingException {
    logger.info("encrtyedUuid : {}", uuid);
    logger.info("username : {}", username);
    logger.info("session : {}", session.getAttribute("user") );
    final String result = userService.login(uuid, username);
    if(isNull(result)) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok().body(result);
  }

}
