package com.mapnote.mapnoteserver.domain.schedule.controller;

import com.mapnote.mapnoteserver.domain.common.dto.ResponseCode;
import lombok.Getter;

@Getter
public enum ScheduleResponseCode implements ResponseCode  {

  CREATE_SUCCESS(201, "S201", "Signup SUCCESS"),
  GET_SCHEDULE_LIST(200, "S202", "Get List SUCCESS");

  private String code;
  private String message;
  private int status;

  ScheduleResponseCode(int status, String code, String message) {
    this.code = code;
    this.message = message;
    this.status = status;
  }
}
