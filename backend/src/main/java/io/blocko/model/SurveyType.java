package io.blocko.model;

import coinstack.paper.model.Code;

public enum SurveyType implements Code {

  /**
   * 비공개 설문.
   */
  Private((byte) 1),

  /**
   * 공개 설문.
   */
  Public((byte) 2);

  private final byte code;

  SurveyType(final byte code) {
    this.code = code;
  }

  public static SurveyType fromCode(final byte code) {
    return Converter.fromCode(values(), code);
  }

  @Override
  public byte code() {
    return code;
  }

}
