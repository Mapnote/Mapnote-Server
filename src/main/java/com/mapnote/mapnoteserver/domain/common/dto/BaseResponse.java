package com.mapnote.mapnoteserver.domain.common.dto;

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

  public BaseResponse(ResponseCode responseCode) {
    this.code = responseCode.getCode();
    this.message = responseCode.getMessage();
    this.status = responseCode.getStatus();
  }

}
