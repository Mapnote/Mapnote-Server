package com.mapnote.mapnoteserver.domain.schedule.dto;

import com.mapnote.mapnoteserver.domain.schedule.entity.Sorting;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;

public class ScheduleRequest {

  @Getter
  public static class Create {

    @Schema(description = "스케줄 내용", example = "생활용품 구매")
    @NotBlank(message = "스케줄 내용은 필수 입니다.")
    private String content;

    @Schema(description = "카테고리(그룹)", example = "LIKE")
    private String category;

    @Schema(description = "주소", example = "영통동 992-9")
    @NotBlank(message = "스케줄이 적용될 주소 정보는 필수 입니다.")
    private String address;

    @Schema(description = "도로명 주소", example = "구일로 4길 65")
    @NotBlank(message = "스케줄이 적용될 도로명 주소 정보는 필수 입니다.")
    private String roadAddress;

    @Schema(description = "장소 이름", example = "영통역 다이소")
    @NotBlank(message = "스케줄이 적용될 장소의 이름은 필수 입니다.")
    private String placeName;

    @Schema(description = "경도", example = "41.40338")
    @NotNull(message = "스케줄이 적용될 위도경도 정보는 필수 입니다.")
    private Double longitude;

    @Schema(description = "위도", example = "2.17403")
    @NotNull(message = "스케줄이 적용될 위도경도 정보는 필수 입니다.")
    private Double latitude;
  }

  @Getter
  public static class Update {

    @Schema(description = "스케줄 내용", example = "생활용품 구매")
    @NotBlank(message = "스케줄 내용은 필수 입니다.")
    private String content;

    @Schema(description = "주소", example = "영통동 992-9")
    @NotBlank(message = "스케줄이 적용될 주소 정보는 필수 입니다.")
    private String address;

    @Schema(description = "도로명 주소", example = "구일로 4길 65")
    @NotBlank(message = "스케줄이 적용될 도로명 주소 정보는 필수 입니다.")
    private String roadAddress;

    @Schema(description = "장소 이름", example = "영통역 다이소")
    @NotBlank(message = "스케줄이 적용될 장소의 이름은 필수 입니다.")
    private String placeName;

    @Schema(description = "경도", example = "41.40338")
    @NotNull(message = "스케줄이 적용될 위도경도 정보는 필수 입니다.")
    private Double longitude;

    @Schema(description = "위도", example = "2.17403")
    @NotNull(message = "스케줄이 적용될 위도경도 정보는 필수 입니다.")
    private Double latitude;
  }

  @Getter
  public static class Location {

    @Schema(description = "경도", example = "41.40338")
    @NotNull(message = "스케줄이 적용될 위도경도 정보는 필수 입니다.")
    private Double longitude;

    @Schema(description = "위도", example = "2.17403")
    @NotNull(message = "스케줄이 적용될 위도경도 정보는 필수 입니다.")
    private Double latitude;
  }

}
