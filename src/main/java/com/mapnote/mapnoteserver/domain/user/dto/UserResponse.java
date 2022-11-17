package com.mapnote.mapnoteserver.domain.user.dto;

import com.mapnote.mapnoteserver.domain.schedule.entity.Schedules;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserResponse {

  @Getter
  @Builder
  @AllArgsConstructor
  public static class TokenResponse {
    @Schema(description = "Grant Type(유형)", example = "Bearer")
    private String grantType;
    @Schema(description = "JWT AT", example = "eyJhbGciOiJIUzI1NiJ9.eyJhdXRoS2V5IjoiaGhxamF0pow3QqLTcL7lU")
    private String accessToken;
    @Schema(description = "JWT RT", example = "eyJhbGciOiJIUzI1NiJ9.-eyJhdXRoS2V5Ipow3QqLTcL7lU")
    private String refreshToken;
    @Schema(description = "RT 만료시간", example = "604800000")
    private Long refreshTokenExpirationTime;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  public static class UserDetailResponse {
    @Schema(description = "이메일", example = "test@gmail.com")
    private String email;
    @Schema(description = "이름(닉네임)", example = "beomsic")
    private String name;
    @Schema(description = "반경", example = "8")
    private Double boundary;
    @Schema(description = "유저가 등록한 스케줄 목록")
    private List<Schedules> scheduleList;
  }

  @Getter
  @AllArgsConstructor
  public static class UserSignupResponse {
    @Schema(description = "유저 UUID", example = "81baa829-7d91-4968-bab0-42fcfd60d79c")
    private UUID id;
  }

}
