package com.mapnote.mapnoteserver.domain.user.controller;


import com.mapnote.mapnoteserver.domain.common.dto.ResponseCode;
import lombok.Getter;

@Getter
public enum UserResponseCode implements ResponseCode {

  LOGIN_SUCCESS(200, "U201", "Login SUCCESS"),
  SIGNUP_SUCCESS(201, "U202", "Signup SUCCESS"),
  REISSUE_TOKEN(200, "U203", "Reissue TOKEN"),
  LOGOUT_SUCCESS(204, "U204", "Logout SUCCESS"),
  CHECK_EMAIL_SUCCESS(200, "U205", "Email is NOT Duplication"),
  PASSWORD_UPDATE_SUCCESS(200, "U206", "Update Password"),
  GET_USER_DETAIL(200, "U207", "Get User Details"),
  USER_INFO_UPDATE_SUCCESS(200, "U208", "Update User Info"),
  USER_DELETE(204, "U209", "Delete User SUCCESS");

  private String code;
  private String message;
  private int status;

  UserResponseCode(int status, String code, String message) {
    this.code = code;
    this.message = message;
    this.status = status;
  }
}
