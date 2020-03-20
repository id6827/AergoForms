package io.blocko.config;

import static java.util.Arrays.stream;
import static org.slf4j.LoggerFactory.getLogger;

import io.blocko.service.account.AuthenticationService;
import io.blocko.service.account.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
@Profile("!insecure")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  protected final Logger logger = getLogger(getClass());

  @Autowired
  protected Environment env;

  @Autowired
  protected UserService userService;

  @Autowired
  protected AuthenticationService authenticationService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new StandardPasswordEncoder();
  }


  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.cors().configurationSource(req -> new CorsConfiguration().applyPermitDefaultValues()).and()
        .authorizeRequests()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/**").permitAll()
        .anyRequest().authenticated();

    http.authorizeRequests()
        .antMatchers(
            "/",
            "/css/**",
            "/fonts/**",
            "/img/**",
            "/js/**",
            "/*.png",
            "/*.html",
            "browserconfig.xml",
            "manifest.json"
          ).permitAll();

    if (stream(env.getActiveProfiles()).filter("development"::equals).findFirst().isPresent()) {
      http.authorizeRequests()
          .antMatchers("/v2/api-docs")
          .permitAll();
      logger.info("API Data not guarded");
    }

    http.formLogin().loginPage("http://localhost:8080/login")
        // .loginProcessingUrl("/loginProcess")
        // .usernameParameter("username")
        // .passwordParameter("password")
        // .defaultSuccessUrl("/forms", true)
        // .successForwardUrl("/users/handle")
        // .failureForwardUrl("/users/handle")
        .permitAll();

    http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
        .invalidateHttpSession(true).permitAll();

    http.headers().frameOptions().disable();

  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationService);
    logger.info("Authentication provider: {}", authenticationService);

    auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOrigin("*");
    configuration.addAllowedMethod("*");
    configuration.addAllowedHeader("*");
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
  
}
