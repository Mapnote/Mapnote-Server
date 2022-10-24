package com.mapnote.mapnoteserver.domain.user.controller;

import com.mapnote.mapnoteserver.domain.user.dto.UserRequest;
import com.mapnote.mapnoteserver.domain.user.dto.UserResponse;
import com.mapnote.mapnoteserver.domain.user.dto.UserResponse.TokenResponse;
import com.mapnote.mapnoteserver.domain.user.service.UserService;
import com.mapnote.mapnoteserver.security.CustomUserDetails;
import com.mapnote.mapnoteserver.security.aop.Auth;
import com.mapnote.mapnoteserver.security.aop.CurrentUser;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("")
  public ResponseEntity<UUID> signUp(@Validated @RequestBody UserRequest.SignUp signUpRequest) {
    UUID id = userService.signUp(signUpRequest);
    return ResponseEntity.created(URI.create("/")).body(id);
  }

  @PostMapping("/login")
  public ResponseEntity<UserResponse.TokenResponse> login(@Validated @RequestBody UserRequest.Login loginRequest) {
    TokenResponse loginResponse = userService.login(loginRequest);
    return ResponseEntity.ok(loginResponse);
  }

  @PostMapping("/reissue")
  public ResponseEntity<UserResponse.TokenResponse> reissue(@Validated @RequestBody UserRequest.Reissue reissueRequest) {
    TokenResponse reissueResponse = userService.reissue(reissueRequest);
    return ResponseEntity.ok(reissueResponse);
  }

  @Auth
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@Validated @RequestBody UserRequest.Logout logoutRequest) {
    userService.logout(logoutRequest);
    return ResponseEntity.noContent().build();
  }

  @Auth
  @PostMapping("/email")
  public ResponseEntity<Void> emailCheck(@Validated @RequestBody UserRequest.Email emailRequest) {
    userService.checkEmail(emailRequest);

    return ResponseEntity.noContent().build();
  }

  @PostMapping("/password")
  public ResponseEntity<Void> renewalPassword(@Validated @RequestBody UserRequest.RenewalPassword renewalPassword) {

    userService.renewalPassword(renewalPassword);

    return ResponseEntity.ok().build();
  }

  @Auth
  @GetMapping("")
  public ResponseEntity<UserResponse.UserDetailResponse> getUserDetail(@CurrentUser CustomUserDetails user) {
    UserResponse.UserDetailResponse userDetail = userService.getUserDetail(user.getId());
    return ResponseEntity.ok(userDetail);
  }

  @Auth
  @PutMapping("/newPassword")
  public ResponseEntity<Void> changePassword(@Validated @RequestBody UserRequest.NewPassword passwordRequest,
      @CurrentUser CustomUserDetails user) {
    userService.changePassword(user.getId(), passwordRequest);
    return ResponseEntity.ok().build();
  }

  @Auth
  @PutMapping("")
  public ResponseEntity<UserResponse.UserDetailResponse> changeInfo(@Validated @RequestBody UserRequest.ChangeInfo changeInfo,
      @CurrentUser CustomUserDetails user) {

    UserResponse.UserDetailResponse userDetail = userService.changeInfo(changeInfo, user.getId());
    return ResponseEntity.ok(userDetail);
  }

  @Auth
  @DeleteMapping("")
  public ResponseEntity<Void> delete(@CurrentUser CustomUserDetails user) {
    userService.delete(user.getId());
    return ResponseEntity.noContent().build();
  }

}
