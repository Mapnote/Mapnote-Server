package com.mapnote.mapnoteserver.domain.alarm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class AlarmRequest {

  @Getter
  public static class CurLocation {

    @Schema(description = "fcm token", example = "생활용품 구매")
    @NotBlank(message = "token 은 필수 입니다.")
    private String token;

    @Schema(description = "경도", example = "41.40338")
    @NotNull(message = "현재 경도 정보는 필수 입니다.")
    private Double longitude;

    @Schema(description = "위도", example = "2.17403")
    @NotNull(message = "현재 위도 정보는 필수 입니다.")
    private Double latitude;
  }


}
