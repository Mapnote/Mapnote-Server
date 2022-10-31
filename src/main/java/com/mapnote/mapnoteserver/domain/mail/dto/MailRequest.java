package com.mapnote.mapnoteserver.domain.mail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class MailRequest {

  @Schema(description = "메일을 받을 이메일 주소", example = "test@gmail.com")
  @NotEmpty(message = "이메일은 필수값 입니다.")
  @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
  private String email;

}
