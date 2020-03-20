package io.blocko.service.account;

import static io.blocko.model.account.AccountStatus.ACTIVE;
import static io.blocko.model.account.AccountStatus.DELETED;
import static io.blocko.model.account.AuthorityCode.USER;
import static java.util.Collections.EMPTY_LIST;
import static java.util.Objects.isNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;
import static org.springframework.data.domain.PageRequest.of;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hera.api.model.AccountAddress;
import hera.api.model.BytesValue;
import hera.api.model.Signature;
import hera.key.AergoSignVerifier;
import hera.key.Verifier;
import io.blocko.exception.DuplicateUsernameException;
import io.blocko.model.Event;
import io.blocko.model.SignUpForm;
import io.blocko.model.UsernameAndPassword;
import io.blocko.model.account.AccountStatus;
import io.blocko.model.account.AuthorityCode;
import io.blocko.model.account.Gender;
import io.blocko.model.account.User;
import io.blocko.repositoy.UserRepository;
import io.blocko.service.AbstractService;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractService implements UserDetailsService {

  private final ObjectMapper mapper = new ObjectMapper();

  private final Map<String, CompletableFuture<User>> id2future = new HashMap<>();

  private final Map<String, User> tmp = new HashMap<>();

  private final Map<String, String> addresses = new HashMap<>();

  private PasswordEncoder passwordEncoder;

  private UserRepository userRepository;

  private AddressService addressService;

  private String endpoint;

  @Autowired
  private AuthenticationService authenticationService;

  /**
   * constructor.
   * 
   * @param passwordEncoder {@link PasswordEncoder}
   * @param userRepository {@link UserRepository}
   * @param addressService {@link AddressService}
   * @throws UnknownHostException {@link UnknownHostException}
   */
  @Autowired
  public UserService(final PasswordEncoder passwordEncoder, final UserRepository userRepository,
      final AddressService addressService) throws UnknownHostException {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.addressService = addressService;

    final User user = new User("admin", passwordEncoder.encode("admin"));
    user.setAuthority(AuthorityCode.ADMIN);
    user.setStatus(AccountStatus.ACTIVE);
    user.setBirtyear(Short.valueOf("2020"));
    user.setGender(Gender.MALE);

    userRepository.save(user);
    InetAddress local = InetAddress.getLocalHost();
    String ip = local.getHostAddress();
    this.endpoint = "http://" + ip + ":9000/event";
  }

  /**
   * SIGNUP PROCESS (1/2) QR출력에 필요한 내용 클라이언트로 출력.
   * 
   * @param signUpForm {@link SignUpForm}
   * @return Object
   * @throws JsonProcessingException {@link JsonProcessingException}
   * @throws DuplicateUsernameException {@link DuplicateUsernameException}
   */
  public Object preSignUp(final SignUpForm signUpForm)
      throws DuplicateUsernameException, JsonProcessingException {
    final User user = new User();
    user.setUsername(signUpForm.getUsername());
    user.setPassword(signUpForm.getPassword());
    user.setBirtyear(signUpForm.getBirthyear());
    user.setGender(Gender.fromCode(signUpForm.getGender()));
    user.setStatus(ACTIVE);
    user.setAuthority(USER);

    if (isUsername(user.getUsername())) {
      throw new DuplicateUsernameException();
    }

    final String requestId = randomUUID().toString();

    final Map<String, String> map = new HashMap<>();
    map.put("uuid", requestId);
    map.put("username", user.getUsername());
    map.put("url", endpoint);

    // User 임시 저장, 메모리릭 문제를 최소화하기 위해 100개 이상 시 초기화.
    if (tmp.size() > 100) {
      tmp.clear();
    }
    tmp.put(requestId, user);

    return map;
  }

  /**
   * SIGNUP PROCESS (2/2) 모바일 인증이 성공한 경우 회원가입 완료. 3분이 넘어가면 해당 요청 취소.
   * 
   * @param event uuid, username
   * @return UserDetails
   * @throws InterruptedException on interupt of Future
   * @throws ExecutionException on failure of Future
   */
  public Object signUp(final Event event) throws InterruptedException, ExecutionException {
    final String uuid = event.getUuid();
    final String username = event.getUsername();
    final Future<User> future = this.execute(() -> {
      logger.info("UUID: {}", uuid);
      return uuid;
    });
    try {
      final User user = future.get(3, TimeUnit.MINUTES);
      logger.info("createNewUser : {}", user);
      final String address = addresses.remove(uuid);
      final String rawPassword = user.getPassword();
      user.setPassword(passwordEncoder.encode(rawPassword));
      userRepository.save(user);
      addressService.save(user, address);
      final Map<String, Object> map = new HashMap<>();
      map.put("address", address);
      map.put("user", authenticate(new UsernameAndPassword(username, rawPassword)));
      return map;
    } catch (TimeoutException e) {
      id2future.remove(uuid);
      tmp.remove(uuid);
    }
    return null;
  }

  /**
   * SIGNIN PROCESS (1/2) QR출력에 필요한 내용 반환.
   * 
   * @param usernameAndPassword username, password
   * @return Object
   */
  public Object preSignIn(final UsernameAndPassword usernameAndPassword) {
    final String username = usernameAndPassword.getUsername();
    final String password = usernameAndPassword.getPassword();
    final User user =
        getUser(username).filter(u -> passwordEncoder.matches(password, u.getPassword()))
            .orElseThrow(() -> new UsernameNotFoundException(username));
    final String requestId = randomUUID().toString();

    final Map<String, String> map = new HashMap<>();
    map.put("uuid", requestId);
    map.put("username", user.getUsername());
    map.put("url", endpoint);

    // User 임시 저장, 메모리릭 문제를 최소화하기 위해 100개 이상 시 초기화.
    if (tmp.size() > 100) {
      tmp.clear();
    }
    user.setPassword(password);
    tmp.put(requestId, user);
    return map;
  }

  /**
   * SIGNIN PROCESS (2/2) 모바일 인증이 성공한 경우 로그인 완료. 3분이 넘어가면 해당 요청 취소.
   * 
   * @param event uuid, username, address, signature
   * @return UserDetails
   * @throws InterruptedException {@link InterruptedException}
   * @throws ExecutionException {@link ExecutionException}
   */
  public Object signIn(final Event event) throws InterruptedException, ExecutionException {
    final String uuid = event.getUuid();
    final String username = event.getUsername();
    final Future<User> future = this.execute(() -> {
      logger.info("UUID: {}", uuid);
      return uuid;
    });
    try {
      final User user = future.get(3, TimeUnit.MINUTES);
      logger.info("try login : {}", user);
      final String rawPassword = user.getPassword();
      
      final Map<String, Object> map = new HashMap<>();
      map.put("address",
          addressService.findByUsername(user, PageRequest.of(0, 1)).getContent().get(0));
      map.put("user", authenticate(new UsernameAndPassword(username, rawPassword)));
      return map;
    } catch (TimeoutException e) {
      id2future.remove(uuid);
      tmp.remove(uuid);
    }
    return null;
  }

  public Boolean isUsername(final String username) {
    return getUser(username).isPresent();
  }

  public Optional<User> getUser(final String username) {
    return userRepository.findByUsername(username);
  }

  /**
   * Get current user.
   *
   * @return current logged-in user
   */
  public Optional<User> getCurrentUser() {
    final SecurityContext securityContext = SecurityContextHolder.getContext();
    final Authentication auth = securityContext.getAuthentication();
    logger.info("Authentication : {}", auth);
    final String username = auth.getName();
    logger.debug("Username: {}", username);
    if (null == username) {
      logger.trace("No logged-in user");
      return empty();
    }

    return userRepository.findByUsername(username);
  }

  protected GrantedAuthority convertAuthority(final AuthorityCode authority) {
    if (null == authority) {
      return null;
    }
    return new SimpleGrantedAuthority("ROLE_" + authority.name());
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    final User user = getUser(username).orElseThrow(() -> new UsernameNotFoundException(username));
    logger.trace("User: {}", user);
    logger.trace("Password: {}", user.getPassword());


    final List authorities = ofNullable(user.getAuthority()).map(this::convertAuthority)
        .map(Arrays::asList).orElse(EMPTY_LIST);

    return new org.springframework.security.core.userdetails.User(user.getUsername(),
        user.getPassword(), user.getStatus() == ACTIVE,
        user.getStatus() != DELETED, true, true, // locked
        authorities);
  }

  /**
   * Search user with {@code partialUsername}.
   *
   * @param partialUsername word to match
   *
   * @return user list
   */
  public List<User> search(final String partialUsername) {
    final PageRequest page = of(0, 7);
    final Page<User> result =
        userRepository.findByUsernameContainingOrderByUsername(partialUsername, page);
    return result.getContent();
  }

  
  /**
   * future 생성.
   * 
   * @param runnable uuid
   * @return Future
   */
  public Future<User> execute(final Supplier<String> runnable) {
    final String requestId = runnable.get();
    if (isNull(requestId)) {
      return null;
    }
    final CompletableFuture<User> future = new CompletableFuture<>();
    id2future.put(requestId, future);
    return future;
  }

  /**
   * 이벤트 처리기.
   * 
   * @param event uuid, address, signature, username 
   * @throws Exception sign검증
   */
  public void handle(final Event event) throws Exception {

    if (null != event.getSignature()) {
      final Verifier verifier = new AergoSignVerifier();
      final AccountAddress addr = new AccountAddress(event.getAddress());
      final Signature sig = new Signature(BytesValue.of(event.getSignature().getBytes()));
      if (!verifier.verify(addr, BytesValue.of(event.getUuid().getBytes()), sig)) {
        throw new Exception();
      }
    }
    final String requestId = event.getUuid();
    final String address = event.getAddress();
    // final String username = event.getUsername();
    logger.info("before id2future : {}", id2future);
    logger.info("before tmp : {}", tmp);
    final Optional<CompletableFuture<User>> futureOpt = ofNullable(id2future.remove(requestId));
    if (futureOpt.isPresent()) {
      addresses.put(requestId, address);
      futureOpt.get().complete(tmp.remove(requestId));
    } else {
      logger.warn("Unknown Event : {}", event);
    }
    logger.info("after id2future : {}", id2future);
    logger.info("after tmp : {}", tmp);
  }

  /**
   * authenticate using username and password.
   * 
   * @param usernameAndPassword username, raw password
   * @return UserDetails
   */
  public UserDetails authenticate(final UsernameAndPassword usernameAndPassword) {
    final String username = usernameAndPassword.getUsername();
    final String password = usernameAndPassword.getPassword();
    final UserDetails userDetails = loadUserByUsername(username);
    final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(userDetails, password,
            userDetails.getAuthorities());

    authenticationService.authenticate(usernamePasswordAuthenticationToken);

    if (usernamePasswordAuthenticationToken.isAuthenticated()) {
      SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      logger.debug(String.format("Auto login %s successfully!", username));
    }
    return userDetails;
  }

}
