package com.mapnote.mapnoteserver.domain.schedule.dto;

import com.mapnote.mapnoteserver.domain.schedule.entity.Category;
import com.mapnote.mapnoteserver.domain.schedule.entity.ScheduleStatus;
import com.mapnote.mapnoteserver.domain.schedule.vo.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ScheduleResponse {

  @Getter
  @Builder
  @AllArgsConstructor
  public static class ScheduleDetail {

    @Schema(description = "스케줄 id", example = "1")
    private Long id;

    @Schema(description = "스케줄 내용", example = "생활용품 구매")
    private String content;

    @Schema(description = "카테고리(그룹)", example = "LIKE")
    private Category category;

    @Schema(description = "스케줄 상태", example = "ONGOING")
    private ScheduleStatus scheduleStatus;

    @Schema(description = "스케줄이 적힌 장소")
    private Place place;

    @Schema(description = "스케줄 작성 시점")
    private LocalDateTime createdAt;

    @Schema(description = "스케줄 최종 수정 시점")
    private LocalDateTime updatedAt;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  public static class ScheduleSummary {

    @Schema(description = "스케줄 id", example = "1")
    private Long id;

    @Schema(description = "스케줄 내용", example = "생활용품 구매")
    private String content;

    @Schema(description = "카테고리(그룹)", example = "LIKE")
    private Category category;

    @Schema(description = "스케줄이 적힌 장소")
    private Place place;
  }

}
