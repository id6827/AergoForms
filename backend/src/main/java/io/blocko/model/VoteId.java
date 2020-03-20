package io.blocko.model;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class VoteId implements Serializable {

  private String voter;
  private String answer;

  public VoteId(String voter, String answer) {
    this.voter = voter;
    this.answer = answer;
  }

}
