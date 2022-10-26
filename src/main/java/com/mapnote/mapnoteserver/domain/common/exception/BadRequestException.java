package com.mapnote.mapnoteserver.domain.common.exception;

public class BadRequestException extends BusinessException {

  public BadRequestException(String message) {
    super(message, ErrorCode.INVALID_INPUT_VALUE);
  }

  public BadRequestException(String message,
      ErrorCode errorCode) {
    super(message, errorCode);
  }
}
