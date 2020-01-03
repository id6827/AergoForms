package io.blocko.repositoy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import io.blocko.model.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
