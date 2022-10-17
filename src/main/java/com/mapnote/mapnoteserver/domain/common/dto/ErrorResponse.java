package com.mapnote.mapnoteserver.domain.common.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

  private String message;

  public ErrorResponse(String message) {
    this.message = message;
  }
}
