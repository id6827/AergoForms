package io.blocko.repositoy;

import io.blocko.model.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, String> {
  
  Page<Survey> findByType(byte surveyType, Pageable pageable);

}
