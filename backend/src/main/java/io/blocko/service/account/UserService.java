package io.blocko.service.account;

import static io.blocko.model.account.AccountStatus.ACTIVE;
import static io.blocko.model.account.AccountStatus.DELETED;
import static io.blocko.model.account.AuthorityCode.USER;
import static java.util.Collections.EMPTY_LIST;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.junit.Assert.assertNotNull;
import static org.springframework.data.domain.PageRequest.of;

import io.blocko.model.UsernameAndPassword;
import io.blocko.model.account.AccountStatus;
import io.blocko.model.account.AuthorityCode;
import io.blocko.model.account.User;
import io.blocko.repositoy.UserRepository;
import io.blocko.service.AbstractService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

@Service
public class UserService extends AbstractService implements UserDetailsService {
  
  protected PasswordEncoder passwordEncoder;

  protected UserRepository userRepository;
  
  @Autowired
  public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    final User user = new User("admin", passwordEncoder.encode("admin"));
    user.setAuthority(AuthorityCode.ADMIN);
    user.setStatus(AccountStatus.ACTIVE);
    userRepository.save(user);
  }

  /**
   * Create new user.
   *
   * @param usernameAndPassword username and password
   */
  public void createNewUser(final UsernameAndPassword usernameAndPassword) {
    final User user = new User();
    user.setUsername(usernameAndPassword.getUsername());
    user.setPassword(passwordEncoder.encode(usernameAndPassword.getPassword()));
    user.setStatus(ACTIVE);
    user.setAuthority(USER);
    
    assertNotNull(user.getUsername());
    assertNotNull(user.getPassword());
    
    userRepository.save(user);
    logger.info("{} created", user);
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
}
