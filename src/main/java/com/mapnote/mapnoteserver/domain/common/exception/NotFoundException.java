package com.mapnote.mapnoteserver.domain.common.exception;

public class NotFoundException extends BusinessException {

  public NotFoundException(String message) {
    super(message, ErrorCode.ENTITY_NOT_FOUND);
  }

  public NotFoundException(String message, ErrorCode errorCode) {
    super(message, errorCode);
  }
}
