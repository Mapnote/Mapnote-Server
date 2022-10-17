package com.mapnote.mapnoteserver.domain.common.exception;

public class UnAuthorizedException extends RuntimeException {

  public UnAuthorizedException(String message) {
    super(message);
  }
}
