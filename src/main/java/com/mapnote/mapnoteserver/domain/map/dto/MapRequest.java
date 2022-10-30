package com.mapnote.mapnoteserver.domain.map.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class MapRequest {

  @Getter
  @Setter
  @ToString
  public static class KeywordQuery{
    @Schema(description = "키워드", example = "노브랜드")
    @NotEmpty(message = "장소 정보는 필수 입력해야합니다.")
    private String query;

    @Schema(description = "사용자의 현재 위치 - 경도", example = "127.08118995506915")
    @NotEmpty(message = "경도는 필수값입니다.")
    private String x;

    @Schema(description = "사용자의 현재 위치 - 위도", example = "37.24291020655134")
    @NotEmpty(message = "위도는 필수값입니다.")
    private String y;

    @Schema(description = "반경", example = "10000")
    @NotNull(message = "반경은 필수값입니다.")
    @Min(value = 1000, message = "반경은 1km 이상이어야 합니다.")
    private Integer radius;
  }

  @Getter
  @Setter
  @ToString
  public static class Coordinate{
    @Schema(description = "경도", example = "127.08118995506915")
    @NotEmpty(message = "경도는 필수값입니다.")
    private String x;

    @Schema(description = "위도", example = "37.24291020655134")
    @NotEmpty(message = "위도는 필수값입니다.")
    private String y;
  }
}
