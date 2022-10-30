package com.mapnote.mapnoteserver.domain.user.controller;

import com.mapnote.mapnoteserver.domain.common.dto.DataResponse;
import com.mapnote.mapnoteserver.domain.common.dto.ErrorResponse;
import com.mapnote.mapnoteserver.domain.user.dto.UserRequest;
import com.mapnote.mapnoteserver.domain.user.dto.UserResponse;
import com.mapnote.mapnoteserver.domain.user.dto.UserResponse.TokenResponse;
import com.mapnote.mapnoteserver.domain.user.dto.UserResponse.UserSignupResponse;
import com.mapnote.mapnoteserver.domain.user.service.UserService;
import com.mapnote.mapnoteserver.security.CustomUserDetails;
import com.mapnote.mapnoteserver.security.aop.Auth;
import com.mapnote.mapnoteserver.security.aop.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "users", description = "유저 API")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Operation(summary = "signup", description = "회원가입 요청")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping("")
  public ResponseEntity<DataResponse<UserResponse.UserSignupResponse>> signUp(@Validated @RequestBody UserRequest.SignUp signUpRequest) {
    UUID id = userService.signUp(signUpRequest);
    DataResponse<UserResponse.UserSignupResponse> response = new DataResponse<>(UserResponseCode.SIGNUP_SUCCESS, new UserSignupResponse(id));
    return ResponseEntity.created(URI.create("/")).body(response);
  }

  @Operation(summary = "login", description = "로그인 요청")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping("/login")
  public ResponseEntity<DataResponse<UserResponse.TokenResponse>> login(@Validated @RequestBody UserRequest.Login loginRequest) {
    TokenResponse loginResponse = userService.login(loginRequest);
    DataResponse<UserResponse.TokenResponse> response = new DataResponse<>(UserResponseCode.LOGIN_SUCCESS, loginResponse);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @Operation(summary = "reissue token", description = "토큰 갱신")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping("/reissue")
  public ResponseEntity<DataResponse<UserResponse.TokenResponse>> reissue(@Validated @RequestBody UserRequest.Reissue reissueRequest) {
    TokenResponse reissueResponse = userService.reissue(reissueRequest);
    DataResponse<UserResponse.TokenResponse> response = new DataResponse<>(UserResponseCode.REISSUE_TOKEN, reissueResponse);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @Operation(summary = "logout", description = "로그아웃 요청")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "UN AUTHORIZATION", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Auth
  @PostMapping("/logout")
  public ResponseEntity<DataResponse<Void>> logout(@Validated @RequestBody UserRequest.Logout logoutRequest) {
    userService.logout(logoutRequest);
    DataResponse<Void> response = new DataResponse<>(UserResponseCode.LOGOUT_SUCCESS,null);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @Operation(summary = "check email duplication", description = "이메일 중복 확인 요청")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping("/email")
  public ResponseEntity<DataResponse<Void>> emailCheck(@Validated @RequestBody UserRequest.Email emailRequest) {
    userService.checkEmail(emailRequest);
    DataResponse<Void> response = new DataResponse<>(UserResponseCode.CHECK_EMAIL_SUCCESS,null);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @Operation(summary = "get user detail", description = "유저의 상세 정보를 요청(마이페이지)")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "UN AUTHORIZATION", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Auth
  @GetMapping("")
  public ResponseEntity<DataResponse<UserResponse.UserDetailResponse>> getUserDetail(@CurrentUser CustomUserDetails user) {
    UserResponse.UserDetailResponse userDetail = userService.getUserDetail(user.getId());
    DataResponse<UserResponse.UserDetailResponse> response = new DataResponse<>(UserResponseCode.GET_USER_DETAIL,userDetail);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @Operation(summary = "renewal password", description = "패스워드를 까먹은 경우 패스워드 재설정 요청")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PutMapping("/renewalPassword")
  public ResponseEntity<DataResponse<Void>> renewalPassword(@Validated @RequestBody UserRequest.RenewalPassword renewalPassword) {

    userService.renewalPassword(renewalPassword);
    DataResponse<Void> response = new DataResponse<>(UserResponseCode.PASSWORD_UPDATE_SUCCESS,null);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @Operation(summary = "modify password", description = "패스워드를 수정하는 요청")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "UN AUTHORIZATION", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Auth
  @PutMapping("/newPassword")
  public ResponseEntity<DataResponse<Void>> changePassword(@Validated @RequestBody UserRequest.NewPassword passwordRequest,
      @CurrentUser CustomUserDetails user) {
    userService.changePassword(user.getId(), passwordRequest);
    DataResponse<Void> response = new DataResponse<>(UserResponseCode.PASSWORD_UPDATE_SUCCESS,null);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @Operation(summary = "modify user info", description = "유저의 정보를 수정하는 요청")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "UN AUTHORIZATION", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Auth
  @PutMapping("")
  public ResponseEntity<DataResponse<UserResponse.UserDetailResponse>> changeInfo(@Validated @RequestBody UserRequest.ChangeInfo changeInfo,
      @CurrentUser CustomUserDetails user) {

    UserResponse.UserDetailResponse userDetail = userService.changeInfo(changeInfo, user.getId());
    DataResponse<UserResponse.UserDetailResponse> response = new DataResponse<>(UserResponseCode.PASSWORD_UPDATE_SUCCESS,userDetail);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @Operation(summary = "delete user", description = "유저 삭제 요청")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "UN AUTHORIZATION", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Auth
  @DeleteMapping("")
  public ResponseEntity<DataResponse<Void>> delete(@CurrentUser CustomUserDetails user) {
    userService.delete(user.getId());
    DataResponse<Void> response = new DataResponse<>(UserResponseCode.USER_DELETE,null);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

}
