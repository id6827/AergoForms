package io.blocko.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
public class Event {

  @Getter
  @Setter
  private String uuid;
  
  @Getter
  @Setter
  private String address;

  @Getter
  @Setter
  private String username;
  
  @Getter
  @Setter
  private String signature;

}
