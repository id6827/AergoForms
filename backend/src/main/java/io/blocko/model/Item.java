package io.blocko.model;

import java.math.BigInteger;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//DTO
@ToString
@NoArgsConstructor
public class Item {
  @Getter
  @Setter
  private String text;

  @Getter
  @Setter
  private BigInteger count;

  public Item(String text) {
    this(text, BigInteger.ZERO);
  }

  public Item(String text, BigInteger count) {
    this.text = text;
    this.count = count;
  }
}
