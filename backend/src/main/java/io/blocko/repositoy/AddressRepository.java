package io.blocko.repositoy;

import io.blocko.model.account.Address;
import io.blocko.model.account.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
  Page<Address> findByUsername(User user, Pageable pageable);

  Optional<Address> findByAddressAndUsername(String address, User user);
}
