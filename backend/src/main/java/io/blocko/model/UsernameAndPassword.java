package io.blocko.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//DTO
@ToString
@NoArgsConstructor
public class UsernameAndPassword {
  @Getter
  @Setter
  protected String username;

  @Getter
  @Setter
  protected String password;

  public UsernameAndPassword(final String username, final String password) {
    this.username = username;
    this.password = password;
  }
}
