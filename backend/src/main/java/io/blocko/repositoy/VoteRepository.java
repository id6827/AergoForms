package io.blocko.repositoy;

import io.blocko.model.Vote;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, String> {

  Optional<Vote> findByVoterIdAndAnswerId(String userId, String anserId);
  
}
