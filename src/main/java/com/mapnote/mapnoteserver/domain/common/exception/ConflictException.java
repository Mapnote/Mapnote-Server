package com.mapnote.mapnoteserver.domain.common.exception;

public class ConflictException extends RuntimeException {

  public ConflictException(String message) {
    super(message);
  }
}
