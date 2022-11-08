package com.mapnote.mapnoteserver.domain.schedule.vo;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

  private String roadNameAddress;
  private String address;

  public Address(String roadNameAddress, String address) {
    this.roadNameAddress = roadNameAddress;
    this.address = address;
  }
}
