package io.blocko.repositoy;

import io.blocko.model.Vote;
import io.blocko.model.VoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, VoteId> {
  
}
