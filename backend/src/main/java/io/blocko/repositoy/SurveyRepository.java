package io.blocko.repositoy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import io.blocko.model.Survey;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

}
