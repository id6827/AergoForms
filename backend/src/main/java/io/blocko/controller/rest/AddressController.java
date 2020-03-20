package io.blocko.controller.rest;

import io.blocko.controller.AbstractController;
import io.blocko.model.account.Address;
import io.blocko.model.account.User;
import io.blocko.service.account.AddressService;
import io.blocko.service.account.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/addresses")
public class AddressController extends AbstractController {

  @Autowired
  protected UserService userService;

  @Autowired
  protected AddressService addressService;

  /**
   * 현재 유저의 어드레스 목록을 반환합니다.
   * 
   * @param page 페이지크기
   * @return List 어드레스 목록
   */
  @GetMapping
  public HttpEntity<List<Address>> addresses(@PageableDefault(size = 7) Pageable page) {
    final Optional<User> currentUser = userService.getCurrentUser();
    logger.debug("currentUser : {}", currentUser);
    return currentUser.map(user -> {
      Page<Address> address = addressService.findByUsername(user, page);
      List<Address> result = address.getContent();
      if (result.isEmpty()) {
        return new ResponseEntity<List<Address>>(HttpStatus.NOT_FOUND);
      }
      logger.debug("Addresses : {}", result);
      return new ResponseEntity<List<Address>>(result, HttpStatus.OK);
    }).orElse(new ResponseEntity<List<Address>>(HttpStatus.BAD_REQUEST));
  }

  /**
   * 어드레스를 등록합니다.
   * 
   * @param address 등록할 어드레스
   * @return Address 등록된 어드레스
   */
  @PostMapping("/register/{address}")
  public HttpEntity<Address> register(@PathVariable(name = "address") String address) {
    final Optional<User> currentUser = userService.getCurrentUser();
    logger.debug("currentUser: {}", currentUser.get());
    return currentUser.map(user -> {
      Address res = addressService.save(user, address);
      return new ResponseEntity<Address>(res, HttpStatus.OK);
    }).orElse(new ResponseEntity<Address>(HttpStatus.BAD_REQUEST));
  }

  /**
   * 어드레스를 제거합니다.
   * 
   * @param address 제거할 어드레스
   * @return HttpStatus 요청에 대한 응답상태
   */
  @DeleteMapping("/delete/{address}")
  public HttpEntity<String> delete(@PathVariable(name = "address") String address) {
    final Optional<User> currentUser = userService.getCurrentUser();
    logger.debug("currentUser: {}", currentUser.get());
    return currentUser.map(user -> {
      addressService.delete(user, address);
      return new ResponseEntity<String>(HttpStatus.OK);
    }).orElse(new ResponseEntity<String>(HttpStatus.BAD_REQUEST));
  }
}
