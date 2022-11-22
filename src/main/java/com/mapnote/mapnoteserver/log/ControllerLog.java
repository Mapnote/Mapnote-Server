package com.mapnote.mapnoteserver.log;

import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ControllerLog {

  private String httpMethod;
  private String urlPattern;
  private ZonedDateTime requestedAt;

  public ControllerLog(String httpMethod, String urlPattern, ZonedDateTime requestedAt) {
    this.httpMethod = httpMethod;
    this.urlPattern = urlPattern;
    this.requestedAt = requestedAt;
  }
}
