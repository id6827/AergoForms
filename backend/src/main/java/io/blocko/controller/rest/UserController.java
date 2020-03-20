package io.blocko.controller.rest;

import static coinstack.paper.ServiceResult.success;
import static java.util.Objects.isNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.blocko.controller.AbstractController;
import io.blocko.exception.DuplicateUsernameException;
import io.blocko.model.Event;
import io.blocko.model.SignUpForm;
import io.blocko.model.UsernameAndPassword;
import io.blocko.model.account.User;
import io.blocko.service.account.UserService;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
public class UserController extends AbstractController {

  @Autowired
  protected UserService userService;

  /**
   * SIGNUP PROCESS (1/2) QR출력에 필요한 내용 클라이언트로 출력.
   * 
   * @param signUpForm username, password, birthyear, gender
   * @return json
   * @throws JsonProcessingException {@link JsonProcessingException}
   * @throws DuplicateUsernameException {@link DuplicateUsernameException}
   * @throws UnknownHostException {@link UnknownHostException}
   */
  @PostMapping("/presignup")
  public HttpEntity<Object> preSignUp(@RequestBody final SignUpForm signUpForm)
      throws JsonProcessingException, DuplicateUsernameException, UnknownHostException {
    logger.info("SignUpForm: {}", signUpForm);
    return ResponseEntity.ok().body(userService.preSignUp(signUpForm));
  }

  /**
   * SIGNUP PROCESS (2/2) 모바일 인증이 성공한 경우 회원가입 완료. 3분이 넘어가면 해당 요청 취소.
   * 
   * @param event uuid, username
   * @return UserDetails
   * @throws InterruptedException on interupt of Future
   * @throws ExecutionException on failure of Future
   */
  @PostMapping
  @Transactional
  public HttpEntity<Object> signUp(@RequestBody final Event event)
      throws InterruptedException, ExecutionException {
    logger.info("event: {}", event);
    final Object result = userService.signUp(event);
    logger.info("is null? {}", isNull(result));
    return ResponseEntity.ok().body(result);
    // return null == result ? ResponseEntity.badRequest().build()
    // : ResponseEntity.status(HttpStatus.CREATED).body(result);
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

  /**
   * SIGNIN PROCESS (1/2) QR출력에 필요한 내용 반환.
   * 
   * @param usernameAndPassword username, password
   * @return json
   */
  @PostMapping("/presignin")
  public HttpEntity<Object> preSignIn(@RequestBody final UsernameAndPassword usernameAndPassword) {
    logger.info("usernameAndPassword : {}", usernameAndPassword);
    return ResponseEntity.ok().body(userService.preSignIn(usernameAndPassword));
  }

  /**
   * SIGNIN PROCESS (2/2) 모바일 인증이 성공한 경우 로그인 완료. 3분이 넘어가면 해당 요청 취소.
   * 
   * @param event uuid, username, address, signature
   * @return UserDetails
   * @throws InterruptedException {@link InterruptedException}
   * @throws ExecutionException {@link ExecutionException}
   */
  @PostMapping("/signin")
  public HttpEntity<Object> signIn(@RequestBody final Event event)
      throws InterruptedException, ExecutionException {
    logger.info("Event : {}", event);

    final Object result = userService.signIn(event);
    logger.info("login result : {}?", result);
    return ResponseEntity.ok().body(result);
    // return null == result ? ResponseEntity.badRequest().build() :
    // ResponseEntity.ok().body(result);
  }


}
