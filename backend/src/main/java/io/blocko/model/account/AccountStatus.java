package io.blocko.model.account;

import coinstack.paper.model.Code;

public enum AccountStatus implements Code {
  /**
   * 상태없음.
   */
  NONE((byte) 0),
  /**
   * 사전등록.
   */
  REGISTERED_IN_ADVANCE((byte) 1),
  /**
   * 승인대기.
   */
  PENDING((byte) 2),
  /**
   * 정상.
   */
  ACTIVE((byte) 10),
  /**
   * 일시정지.
   */
  SUSPENDED((byte) 20),
  /**
   * 삭제.
   */
  DELETED((byte) 21);

  private final byte code;

  AccountStatus(final byte code) {
    this.code = code;
  }

  public static AccountStatus fromCode(final byte code) {
    return Converter.fromCode(AccountStatus.values(), code);
  }

  @Override
  public byte code() {
    return code;
  }
}
