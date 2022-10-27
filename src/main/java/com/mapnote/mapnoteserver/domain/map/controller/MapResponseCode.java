package com.mapnote.mapnoteserver.domain.map.controller;

import com.mapnote.mapnoteserver.domain.common.dto.ResponseCode;
import lombok.Getter;

@Getter
public enum MapResponseCode implements ResponseCode {

  SEARCH_SUCCESS(200, "M201", "Search Place SUCCESS");

  private String code;
  private String message;
  private int status;

  MapResponseCode(int status, String code, String message){
    this.code = code;
    this.message = message;
    this.status = status;
  }
}
