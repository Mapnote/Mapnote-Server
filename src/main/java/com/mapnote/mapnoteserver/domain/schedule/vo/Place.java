package com.mapnote.mapnoteserver.domain.schedule.vo;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {

  private String name;
  private Double longitude;
  private Double latitude;

  @Embedded
  private Address address;

  public Place(String name, Double longitude, Double latitude,
      Address address) {
    this.name = name;
    this.longitude = longitude;
    this.latitude = latitude;
    this.address = address;
  }
}
