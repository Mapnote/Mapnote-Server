package com.mapnote.mapnoteserver.domain.common.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DataResponse<T> extends BaseResponse {

  private T data;

  public DataResponse(T data) {
    super();
    this.data = data;
  }

  public DataResponse(ResponseCode responseCode, T data) {
    super(responseCode);
    this.data = data;
  }
}
