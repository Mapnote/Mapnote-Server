package com.mapnote.mapnoteserver.domain.alarm.dto;

import com.mapnote.mapnoteserver.domain.schedule.entity.Category;
import com.mapnote.mapnoteserver.domain.schedule.entity.ScheduleStatus;
import com.mapnote.mapnoteserver.domain.schedule.vo.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class AlarmResponse {

  @Getter
  @Builder
  @AllArgsConstructor
  public static class FireBaseToken {

    @Schema(description = "fcm token", example = "bdksljfgjkl2jqklr123")
    private String token;

  }


}
