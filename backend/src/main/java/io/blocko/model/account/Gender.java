package io.blocko.model.account;

import coinstack.paper.model.Code;

public enum Gender implements Code {
  /**
   * 남자.
   */
  MALE((byte) 0),
  /**
   * 여자.
   */
  FEMALE((byte) 1);

  private final byte code;

  Gender(final byte code) {
    this.code = code;
  }

  public static Gender fromCode(final byte code) {
    return Converter.fromCode(Gender.values(), code);
  }

  @Override
  public byte code() {
    return code;
  }
}
