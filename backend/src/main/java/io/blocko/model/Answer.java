package io.blocko.model;

import coinstack.paper.model.UuidEntity;
import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ANSWER")
@NoArgsConstructor
@ToString(callSuper = true)
public class Answer extends UuidEntity {

  @Getter
  @Setter
  private String text;

  @Getter
  @Setter
  private BigInteger count;

  public Answer(String text) {
    this(text, BigInteger.ZERO);
  }

  public Answer(String text, BigInteger count) {
    this.text = text;
    this.count = count;
  }

  /**
   * Increment.
   * 
   * @return BigInteger
   */
  public BigInteger inc() {
    this.count = count.add(BigInteger.ONE);
    return count;
  }

  /**
   * Decrement.
   * 
   * @return BigInteger
   */
  public BigInteger dec() {
    this.count = 0 == count.compareTo(BigInteger.ZERO) ? BigInteger.ZERO
        : count.add(BigInteger.ONE.negate());
    return count;
  }
}
