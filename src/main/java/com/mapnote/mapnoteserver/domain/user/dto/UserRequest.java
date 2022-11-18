package com.mapnote.mapnoteserver.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class UserRequest {

  @Getter
  public static class SignUp {
    @Schema(description = "이메일", example = "test@gmail.com")
    @NotEmpty(message = "이메일은 필수 입력해야합니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @Schema(description = "비밀번호", example = "test123!!")
    @NotEmpty(message = "비밀번호는 필수 입력해야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @Schema(description = "이름(닉네임)", example = "beomsic")
    private String name;
  }

  @Getter
  public static class Login {
    @Schema(description = "이메일", example = "test@gmail.com")
    @NotEmpty(message = "이메일은 필수 입력해야합니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @Schema(description = "비밀번호", example = "test123!!")
    @NotEmpty(message = "비밀번호는 필수 입력해야 합니다.")
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
      return new UsernamePasswordAuthenticationToken(email, password);
    }
  }

  @Getter
  public static class Reissue {
    @Schema(description = "JWT AT", example = "eyJhbGciOiJIUzI1NiJ9.eyJhdXRoS2V5IjoiaGhxamF0pow3QqLTcL7l")
    @NotEmpty(message = "accessToken 값을 입력해야 합니다.")
    private String accessToken;

    @Schema(description = "JWT RT", example = "eyJhbGciOiJIUzI1NiJ9.-eyJhdXRoS2V5Ipow3QqLTcL7lU")
    @NotEmpty(message = "refreshToken 값을 입력해야 합니다.")
    private String refreshToken;
  }

  @Getter
  public static class Logout {
    @Schema(description = "JWT AT", example = "eyJhbGciOiJIUzI1NiJ9.eyJhdXRoS2V5IjoiaGhxamF0pow3QqLTcL7lU")
    @NotEmpty()
    private String accessToken;

    @Schema(description = "JWT RT", example = "eyJhbGciOiJIUzI1NiJ9.eyJhdXRoS2V5Ipow3QqLTcL7lU")
    @NotEmpty()
    private String refreshToken;
  }

  @Getter
  public static class Email {
    @Schema(description = "이메일", example = "test@gmail.com")
    @NotEmpty(message = "이메일은 필수값 입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;
  }

  @Getter
  public static class NewPassword {

    @Schema(description = "기존 비밀번호", example = "test123!!")
    @NotEmpty(message = "기존 비밀번호는 필수 입력해야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "기존 비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String oldPassword;

    @Schema(description = "새로운 비밀번호", example = "test123!!")
    @NotEmpty(message = "새로운 비밀번호는 필수 입력해야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "바꿀 비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String newPassword;
  }

  @Getter
  public static class ChangeInfo {
    @Schema(description = "이름(닉네임)", example = "beomsic")
    private String name;

    @Schema(description = "반경", example = "8")
    private Double boundary;

  }

  @Getter
  public static class RenewalPassword {
    @Schema(description = "이메일", example = "test@gmail.com")
    @NotEmpty(message = "이메일은 필수값 입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @Schema(description = "새로운 비밀번호", example = "test123!!")
    @NotEmpty(message = "새로운 비밀번호는 필수 입력해야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "바꿀 비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String newPassword;

  }
}
