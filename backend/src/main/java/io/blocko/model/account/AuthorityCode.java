package io.blocko.model.account;

import coinstack.paper.model.Code;

public enum AuthorityCode implements Code {
  /**
   * 없음.
   */
  NONE((byte) 0),

  /**
   * 일반 사용자.
   */
  USER((byte) 10),


  /**
   * 관리자.
   */
  ADMIN((byte) 99);

  private final byte code;

  AuthorityCode(final byte code) {
    this.code = code;
  }

  public static AuthorityCode fromCode(final byte code) {
    return Converter.fromCode(values(), code);
  }

  @Override
  public byte code() {
    return code;
  }
}
