package io.blocko.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//DTO
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {
  @Getter
  @Setter
  protected String username;

  @Getter
  @Setter
  protected String password;
  
  @Getter
  @Setter
  protected Short birthyear;
  
  @Getter
  @Setter
  protected byte gender;
  
}
