package com.mapnote.mapnoteserver.domain.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.mapnote.mapnoteserver.domain.common.dto.ResponseCode;
import lombok.Getter;

@JsonFormat(shape = Shape.OBJECT)
@Getter
public enum ErrorCode implements ResponseCode {

  // common
  INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
  METHOD_NOT_ALLOWED(405, "C002", " Call Invalid Wrong Method"),
  ENTITY_NOT_FOUND(404, "C003", " Entity Not Found"),
  INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
  INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
  HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),
  CONFLICT_INPUT_VALUE(409, "C007", "Conflict with the Current State"),
  UNAUTHORIZED_REQUEST(401, "C008", "UnAuthentication credentials for Request"),

  // User
  LOGIN_INPUT_INVALID(400, "U001", "Login Input is invalid"),
  EMAIL_DUPLICATION(409, "U002", "Email is Duplication"),
  NOT_FOUND_USER(404, "U003", "User does not exist"),
  PASSWORD_DUPLICATION(409, "U004", "Password is Duplication"),
  WRONG_PASSWORD_INPUT(400, "U005", "Invalid password entered."),
  WRONG_INPUT_INVALID(400, "U006", "Invalid Input entered.");


  // Memo


  private String code;
  private String message;
  private int status;

  ErrorCode(int status, String code, String message) {
    this.code = code;
    this.message = message;
    this.status = status;
  }
}
