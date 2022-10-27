package com.mapnote.mapnoteserver.domain.memo.dto;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class MemoRequest {

  @Getter
  public static class Save {
    private Double latitude;
    private Double longitude;
    private String address;
    private String contents;
    private MemoType type;
  }
  @Getter
  public static class Delete {
    private Long id;
  }


}
