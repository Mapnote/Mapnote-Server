package com.mapnote.mapnoteserver.domain.common.exception;

public class ConflictException extends BusinessException {

  public ConflictException(String message) {
    super(message, ErrorCode.CONFLICT_INPUT_VALUE);
  }

  public ConflictException(String message, ErrorCode errorCode) {
    super(message, errorCode);
  }
}
