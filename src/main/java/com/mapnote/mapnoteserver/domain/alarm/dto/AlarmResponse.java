package com.mapnote.mapnoteserver.domain.alarm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class AlarmResponse {

  @Getter
  @Builder
  @AllArgsConstructor
  public static class FireBaseToken {

    @Schema(description = "fcm token", example = "bdksljfgjkl2jqklr123")
    private String token;

  }


}
