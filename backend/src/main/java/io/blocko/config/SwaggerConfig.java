package io.blocko.config;

import static springfox.documentation.builders.PathSelectors.any;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  /**
   * Configure swagger2.
   *
   * @return Docket
   */
  @Bean
  public Docket api() {
    return new Docket(SWAGGER_2).select()
        .apis(basePackage("io.blocko.controller.rest"))
        .paths(any())
        .build();
  }
}


