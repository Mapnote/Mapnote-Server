package com.mapnote.mapnoteserver.domain.common.exception;

public class UnAuthorizedException extends BusinessException {

  public UnAuthorizedException(String message) {
    super(message, ErrorCode.UNAUTHORIZED_REQUEST);
  }

  public UnAuthorizedException(String message,
      ErrorCode errorCode) {
    super(message, errorCode);
  }
}
