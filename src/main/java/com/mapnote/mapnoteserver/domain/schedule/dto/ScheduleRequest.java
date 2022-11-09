package com.mapnote.mapnoteserver.domain.schedule.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;

public class ScheduleRequest {

  @Getter
  public static class Create {

    @NotBlank(message = "스케줄 내용은 필수 입니다.")
    private String content;

    private String category;

    @NotBlank(message = "스케줄이 적용될 주소 정보는 필수 입니다.")
    private String address;

    @NotBlank(message = "스케줄이 적용될 도로명 주소 정보는 필수 입니다.")
    private String roadAddress;

    @NotBlank(message = "스케줄이 적용될 장소의 이름은 필수 입니다.")
    private String placeName;

    @NotNull(message = "스케줄이 적용될 위도경도 정보는 필수 입니다.")
    private Double longitude;

    @NotNull(message = "스케줄이 적용될 위도경도 정보는 필수 입니다.")
    private Double latitude;
  }

  @Getter
  public static class Update {

    @NotBlank(message = "스케줄 내용은 필수 입니다.")
    private String content;

    @NotBlank(message = "스케줄이 적용될 주소 정보는 필수 입니다.")
    private String address;

    @NotBlank(message = "스케줄이 적용될 도로명 주소 정보는 필수 입니다.")
    private String roadAddress;

    @NotBlank(message = "스케줄이 적용될 장소의 이름은 필수 입니다.")
    private String placeName;

    @NotNull(message = "스케줄이 적용될 위도경도 정보는 필수 입니다.")
    private Double longitude;

    @NotNull(message = "스케줄이 적용될 위도경도 정보는 필수 입니다.")
    private Double latitude;
  }
}
