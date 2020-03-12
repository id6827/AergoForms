package io.blocko.config;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@EnableWebSecurity
@Profile({"insecure", "test"})
public class SecurityDisabledConfig extends WebSecurityConfigurerAdapter {
  protected final Logger logger = getLogger(getClass());

  @Bean
  public PasswordEncoder getLoginPasswordEncoder() {
    return new StandardPasswordEncoder();
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.authorizeRequests().anyRequest().permitAll();
    http.headers().frameOptions().disable();
  }
}
