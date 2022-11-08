package com.mapnote.mapnoteserver.domain.user.util;

import com.mapnote.mapnoteserver.domain.user.dto.UserResponse;
import com.mapnote.mapnoteserver.domain.user.dto.UserResponse.UserDetailResponse;
import com.mapnote.mapnoteserver.domain.user.entity.User;

public class UserConverter {

  public static UserResponse.UserDetailResponse toUserDetail(User user) {
    return UserDetailResponse.builder()
        .email(user.getEmail())
        .name(user.getName())
        .scheduleList(user.getSchedules())
        .boundary(user.getBoundary())
        .build();
  }

}
