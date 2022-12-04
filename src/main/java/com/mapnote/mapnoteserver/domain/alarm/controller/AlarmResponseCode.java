package com.mapnote.mapnoteserver.domain.alarm.controller;

import com.mapnote.mapnoteserver.domain.common.dto.ResponseCode;
import lombok.Getter;

@Getter
public enum AlarmResponseCode implements ResponseCode  {

  SEND_ALARM(200, "S200", "Send Alarm SUCCESS"),
  GET_FIREBASE_TOKEN(200, "S201", "Get Token SUCCESS");

  private String code;
  private String message;
  private int status;

  AlarmResponseCode(int status, String code, String message) {
    this.code = code;
    this.message = message;
    this.status = status;
  }
}
