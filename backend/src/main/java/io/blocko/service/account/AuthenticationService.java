package io.blocko.service.account;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService extends DaoAuthenticationProvider {

  protected final Logger logger = getLogger(getClass());

  @Autowired
  @Override
  public void setPasswordEncoder(final PasswordEncoder passwordEncoder) {
    super.setPasswordEncoder(passwordEncoder);
  }

  @Autowired
  @Override
  public void setUserDetailsService(final UserDetailsService userDetailsService) {
    super.setUserDetailsService(userDetailsService);
  }

  @Override
  public Authentication authenticate(final Authentication authentication)
      throws AuthenticationException {
    logger.info("Authentication: {}", authentication);

    final String decodedPassword = (String) authentication.getCredentials();
//    logger.debug("Decrypted password: {}", decodedPassword);
    logger.info("Decrypted password: {}", decodedPassword);

    final UsernamePasswordAuthenticationToken decodedAuthentication =
        new UsernamePasswordAuthenticationToken(authentication.getName(), decodedPassword);
//    logger.trace("Trying to authenticate... {}", decodedAuthentication);
    logger.info("Trying to authenticate... {}", decodedAuthentication);
    
    logger.info("super !!! : {} ", super.authenticate(decodedAuthentication));
    return super.authenticate(decodedAuthentication);
  }
}
