package io.blocko.config;

import io.blocko.model.Answer;
import io.blocko.model.Survey;
import io.blocko.model.Vote;
import io.blocko.model.account.Address;
import io.blocko.model.account.User;
import io.blocko.repositoy.AddressRepository;
import io.blocko.repositoy.AnswerRepository;
import io.blocko.repositoy.SurveyRepository;
import io.blocko.repositoy.UserRepository;
import io.blocko.repositoy.VoteRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackageClasses = {Jsr310JpaConverters.class, Address.class, User.class,
    Answer.class, Survey.class, Vote.class})
@EnableJpaRepositories(basePackageClasses = {AddressRepository.class, AnswerRepository.class,
    SurveyRepository.class, UserRepository.class, VoteRepository.class})
@EnableJpaAuditing
public class JpaConfig {

}
