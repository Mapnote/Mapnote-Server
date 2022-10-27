package com.mapnote.mapnoteserver.domain.memo.controller;


import com.mapnote.mapnoteserver.domain.common.dto.ResponseCode;
import lombok.Getter;

@Getter
public enum MemoResponseCode implements ResponseCode {

  LOAD_SUCCESS(200, "M201", "Load SUCCESS"),
  SAVE_SUCCESS(200, "M202", "Save SUCCESS"),
  DELETE_SUCCESS(200, "M203", "Delete SUCCESS"),
  UPDATE_SUCCESS(200, "M204", "Update SUCCESS");
  private String code;
  private String message;
  private int status;

  MemoResponseCode(int status, String code, String message) {
    this.code = code;
    this.message = message;
    this.status = status;
  }
}
