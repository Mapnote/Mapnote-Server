package com.mapnote.mapnoteserver.security.jwt;

public enum JwtExpiration {

  ACCESS_TOKEN_EXPIRATION_TIME("JWT AccessToken 만료 시간 / 30분", 1000L * 60 * 30),
  REFRESH_TOKEN_EXPIRATION_TIME("JWT refreshToken 만료 시간 / 7", 1000L * 60 * 60 * 24 * 7),
  LOGIN_EXPIRATION_TIME("로그인 횟수시도 만료 시간 / 6시간 ", 1000L * 60 * 60 * 6),
  BAN_EXPIRATION_TIME("로그인 금지 만료 시간 / 1일", 1000L * 60 * 60 * 24);

  private String description;
  private Long expirationTime;

  JwtExpiration(String description, Long expirationTime) {
    this.description = description;
    this.expirationTime = expirationTime;
  }

  public Long getExpirationTime() {
    return expirationTime;
  }
}
