package com.mapnote.mapnoteserver.domain.common.dto;

import com.mapnote.mapnoteserver.domain.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class BaseResponse {

  private String code;
  private String message;
  private int status;

  public BaseResponse() {
    this.code = "C000";
    this.message = "SUCCESS";
  }

  public BaseResponse(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public BaseResponse(ErrorCode errorCode) {
    this.code = errorCode.getCode();
    this.message = errorCode.getMessage();
    this.status = errorCode.getStatus();
  }

}
