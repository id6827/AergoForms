package io.blocko.controller.rest;

import static coinstack.paper.ServiceResult.success;

import io.blocko.controller.AbstractController;
import io.blocko.model.UsernameAndPassword;
import io.blocko.model.account.User;
import io.blocko.service.account.UserService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
public class UserController extends AbstractController {

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
  public Object createNewUser(@RequestBody final UsernameAndPassword usernameAndPassword,
      HttpServletResponse response) throws Exception {
    logger.trace("Username: {}", usernameAndPassword);
    userService.createNewUser(usernameAndPassword);
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

}
