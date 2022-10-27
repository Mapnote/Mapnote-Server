package com.mapnote.mapnoteserver.domain.map.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

public class MapRequest {

  @Getter
  @ToString
  public static class KeywordQuery{
    @NotEmpty(message = "장소 정보는 필수 입력해야합니다.")
    private String query;
    @NotEmpty(message = "경도는 필수값입니다.")
    private String x;
    @NotEmpty(message = "위도는 필수값입니다.")
    private String y;
    @NotNull(message = "반경은 필수값입니다.")
    @Min(value = 1000, message = "반경은 1km 이상이어야 합니다.")
    private Integer radius;
  }

  @Getter
  @ToString
  public static class Coordinate{
    @NotEmpty(message = "경도는 필수값입니다.")
    private String x;
    @NotEmpty(message = "위도는 필수값입니다.")
    private String y;
  }
}
