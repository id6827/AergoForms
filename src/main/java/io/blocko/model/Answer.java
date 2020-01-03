package io.blocko.model;

import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table
@Entity
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Answer {
  @Id
  @GeneratedValue(strategy=GenerationType.SEQUENCE)
  private Long id;
  private String text;
  private BigInteger count;

  public Answer(String text) {
    this(text, BigInteger.ZERO);
  }

  public Answer(String text, BigInteger count) {
    this.text = text;
    this.count = count;
  }

  // Increment
  public BigInteger inc() {
    this.count = count.add(BigInteger.ONE);
    return count;
  }

  // Decrement{
  public BigInteger dec() {
    this.count = 0 == count.compareTo(BigInteger.ZERO) ? BigInteger.ZERO
        : count.add(BigInteger.ONE.negate());
    return count;
  }
}
