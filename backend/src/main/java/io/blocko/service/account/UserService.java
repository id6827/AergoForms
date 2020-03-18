package io.blocko.service.account;

import static io.blocko.model.account.AccountStatus.ACTIVE;
import static io.blocko.model.account.AccountStatus.DELETED;
import static io.blocko.model.account.AuthorityCode.USER;
import static java.util.Collections.EMPTY_LIST;
import static java.util.Objects.isNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertNotNull;
import static org.springframework.data.domain.PageRequest.of;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import hera.api.model.AccountAddress;
import hera.key.AergoSignVerifier;
import hera.key.Verifier;
import io.blocko.model.Event;
import io.blocko.model.SignUpForm;
import io.blocko.model.account.AccountStatus;
import io.blocko.model.account.Address;
import io.blocko.model.account.AuthorityCode;
import io.blocko.model.account.Gender;
import io.blocko.model.account.User;
import io.blocko.repositoy.UserRepository;
import io.blocko.service.AbstractService;

@Service
public class UserService extends AbstractService implements UserDetailsService {

  protected final ObjectMapper mapper = new ObjectMapper();
  
  protected final Map<String, CompletableFuture<Event>> id2future = new HashMap<>();
  
  protected PasswordEncoder passwordEncoder;
  
  protected UserRepository userRepository;
  
  @Autowired
  protected AddressService addressService;
  
  @Autowired
  public UserService(final PasswordEncoder passwordEncoder, final UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    final User user = new User("admin", passwordEncoder.encode("admin"));
    user.setAuthority(AuthorityCode.ADMIN);
    user.setStatus(AccountStatus.ACTIVE);
    user.setBirtyear(Short.valueOf("2020"));
    user.setGender(Gender.MALE);
    userRepository.save(user);
  }

  /**
   * Create new user.
   *
   * @param usernameAndPassword username and password
   * @throws ExecutionException 
   * @throws InterruptedException 
   */
  public void createNewUser(final SignUpForm signUpForm) throws InterruptedException, ExecutionException {
    final User user = new User();
    user.setUsername(signUpForm.getUsername());
    user.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
    user.setBirtyear(signUpForm.getBirtyear());
    user.setGender(Gender.fromCode(signUpForm.getGender()));
    user.setStatus(ACTIVE);
    user.setAuthority(USER);
    
    assertNotNull(user.getUsername());
    assertNotNull(user.getPassword());
    
    final Future<Event> eventFuture = this.execute(() -> {
      final String uuid = randomUUID().toString();
      logger.info("UUID: {}", uuid);
      userRepository.save(user);
      logger.info("{} created", user);
      return uuid;
    });
    
    eventFuture.get();
  }
  
  public String login(final String uuid, final String username) throws JsonProcessingException {
//    final User user = new User();
//    uer.setUsername(username);
      final User user = getUser(username).orElseThrow(() -> new UsernameNotFoundException(username));
//    Address address = addressService.findByUsername(user, PageRequest.of(0, 1)).getContent().get(0);
//    Verifier verifier = new AergoSignVerifier();
//    
//    AccountAddress accountAddress = new AccountAddress(address.getAddress());
//    verifier.verify(accountAddress, message, signature)
    
//    eventFuture.get();
    final Optional<CompletableFuture<Event>> futureOpt = ofNullable(id2future.remove(uuid));
    if(futureOpt.isPresent()) {
      futureOpt.get();
    } else {
      logger.warn("Unknown Event : {}", uuid);
    }
    
    final Map<String, String> map = new HashMap<>();
    map.put("username", user.getUsername());
    map.put("authority", user.getAuthority().toString());
    map.put("birtyear", user.getBirtyear().toString());
    map.put("gender", user.getGender().toString());
    map.put("status", user.getStatus().toString());
    return mapper.writeValueAsString(map);
    
    
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
        user.getPassword(), user.getStatus() == ACTIVE, user.getStatus() != DELETED, true, true, // locked
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

  public Future<Event> execute(final Supplier<String> runnable) {
    final String requestId = runnable.get();
    if (isNull(requestId)) {
      return null;
    }
    final CompletableFuture<Event> future = new CompletableFuture<>();
    id2future.put(requestId, future);
    return future;
  }
  
  // {"UDEvent": "createUser", "data" : "{ \"uuid\" : \"a9a84af7-529b-4f4a-aec8-4f1440e8c706\", \"result\" : \"success\"}"}
  public void handle(final String eventStr) throws JsonParseException, JsonMappingException, IOException {
    final Event event = mapper.readValue(eventStr, Event.class);
    final String requestId = event.getAttribute("uuid");
    logger.info("id2future : {}", id2future);
    final Optional<CompletableFuture<Event>> futureOpt = ofNullable(id2future.remove(requestId));
    if(futureOpt.isPresent()) {
      futureOpt.get().complete(event);
    } else {
      logger.warn("Unknown Event : {}", event);
    }
  }
  
  
}
