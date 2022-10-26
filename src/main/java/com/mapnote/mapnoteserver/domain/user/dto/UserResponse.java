package com.mapnote.mapnoteserver.domain.user.dto;

import com.mapnote.mapnoteserver.domain.memo.entity.Memo;
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
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long refreshTokenExpirationTime;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  public static class UserDetailResponse {
    private String email;
    private String name;
    private Long boundary;
    private List<Memo> memoList;
  }

  @Getter
  @AllArgsConstructor
  public static class UserSignupResponse {
    private UUID id;
  }

}
