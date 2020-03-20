package io.blocko.service.account;

import io.blocko.model.account.Address;
import io.blocko.model.account.User;
import io.blocko.repositoy.AddressRepository;
import io.blocko.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AddressService extends AbstractService {

  @Autowired
  protected AddressRepository addressRepository;

  public Page<Address> findByUsername(User user, Pageable pageable) {
    return addressRepository.findByUsername(user, pageable);
  }

  public Address save(User user, String address) {
    logger.info("ADDRESS-SERVICE SAVE : username {}, address {}", user.getUsername(), address);
    return addressRepository.save(new Address(user, address));
  }

  /**
   * 어드레스를 제거합니다.
   * 
   * @param user 유저정보
   * @param address 제거할 어드레스
   */
  public void delete(User user, String address) {
    addressRepository.findByAddressAndUsername(address, user).ifPresent(addr -> {
      logger.debug("ADDRESS-SERVICE DELETE : {}", addr);
      addressRepository.delete(addr);
    });
  }
}
