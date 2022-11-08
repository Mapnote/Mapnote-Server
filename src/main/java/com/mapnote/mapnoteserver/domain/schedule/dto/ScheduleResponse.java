package com.mapnote.mapnoteserver.domain.schedule.dto;

import com.mapnote.mapnoteserver.domain.schedule.entity.Category;
import com.mapnote.mapnoteserver.domain.schedule.entity.ScheduleStatus;
import com.mapnote.mapnoteserver.domain.schedule.vo.Place;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ScheduleResponse {

  @Getter
  @Builder
  @AllArgsConstructor
  public static class ScheduleDetail {

    private String content;

    private Category category;

    private ScheduleStatus scheduleStatus;

    private Place place;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
  }

}
