package com.mapnote.mapnoteserver.domain.map.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

public class KakaoApiResponse {

  @Getter
  @ToString
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class KeywordResult{
    private List<Place> documents;

    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Place{
      private String place_name;
      private String address_name;
      private String road_address_name;
      private String x;
      private String y;
    }
  }

  @Getter
  @ToString
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class CoordinateResult{
    private Meta meta;
    private List<Addresses> documents;

    // total_count가 0이면 프론트에 400을 보내줌
    @Getter
    @ToString
    public static class Meta{
      private int total_count;
    }
  }

  @Getter
  @ToString
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Addresses{
    private RoadAddress road_address;
    private Address address;

    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address{
      private String address_name;
    }

    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RoadAddress{
      private String address_name;
    }
  }
}
