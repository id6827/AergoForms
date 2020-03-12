package io.blocko.repositoy;

import io.blocko.model.account.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByUsername(String username);

  Page<User> findByUsernameContainingOrderByUsername(String username, Pageable pageable);
}
