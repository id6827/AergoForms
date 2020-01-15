package io.blocko.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import io.blocko.model.Answer;
import io.blocko.model.Survey;
import io.blocko.repositoy.AnswerRepository;
import io.blocko.repositoy.SurveyRepository;

@Configuration
@EntityScan(basePackageClasses = {Jsr310JpaConverters.class, Survey.class, Answer.class})
@EnableJpaRepositories(basePackageClasses = {SurveyRepository.class, AnswerRepository.class})
@EnableJpaAuditing
public class JpaConfig {

}
