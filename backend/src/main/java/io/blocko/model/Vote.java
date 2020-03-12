package io.blocko.model;

import io.blocko.model.account.User;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "VOTE")
@NoArgsConstructor
@IdClass(VoteId.class)
@ToString(callSuper = false)
public class Vote {

  @Id
  @Getter
  @Setter
  @OneToOne
  @JoinColumn(name = "USER_UUID", referencedColumnName = "UUID", nullable = false)
  protected User voter;

  @Id
  @Getter
  @Setter
  @OneToOne
  @JoinColumn(name = "ANSWER_UUID", referencedColumnName = "UUID", nullable = false)
  protected Answer answer;

  public Vote(final User voter, final Answer answer) {
    this.voter = voter;
    this.answer = answer;
  }

}
